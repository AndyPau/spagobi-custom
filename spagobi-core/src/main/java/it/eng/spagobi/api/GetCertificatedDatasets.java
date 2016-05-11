/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.api;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.execution.service.ExecuteAdHocUtility;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.serializer.SerializerFactory;
import it.eng.spagobi.container.ObjectUtils;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.ckan.CKANClient;
import it.eng.spagobi.tools.dataset.ckan.CKANConfig;
import it.eng.spagobi.tools.dataset.ckan.Connection;
import it.eng.spagobi.tools.dataset.ckan.exception.CKANException;
import it.eng.spagobi.tools.dataset.ckan.resource.impl.Resource;
import it.eng.spagobi.tools.dataset.ckan.utils.CKANUtils;
import it.eng.spagobi.tools.dataset.dao.IDataSetDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;
import it.eng.spagobi.utilities.json.JSONUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Davide Zerbetto (davide.zerbetto@eng.it)
 *
 * @deprecated Use specific services exposed by DataSetResource
 *
 */
@Deprecated
@Path("/certificateddatasets")
public class GetCertificatedDatasets {

	static private Logger logger = Logger.getLogger(GetCertificatedDatasets.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public String getAllDataSet(@Context HttpServletRequest request) {
		IDataSetDAO dataSetDao = null;
		List<IDataSet> dataSets = new ArrayList<IDataSet>();

		IEngUserProfile profile = (IEngUserProfile) request.getSession().getAttribute(IEngUserProfile.ENG_USER_PROFILE);

		// FilterIOManager ioManager = new FilterIOManager(request, null);
		// ioManager.initConetxtManager();
		// IEngUserProfile profile = (IEngUserProfile) ioManager.getFromSession(IEngUserProfile.ENG_USER_PROFILE);

		JSONObject JSONReturn = new JSONObject();
		JSONArray datasetsJSONArray = new JSONArray();
		JSONArray ckanJSONArray = new JSONArray();
		try {
			dataSetDao = DAOFactory.getDataSetDAO();
			dataSetDao.setUserProfile(profile);

			String isTech = request.getParameter("isTech");
			String allMyDataDS = request.getParameter("allMyDataDs");
			String ckanDS = request.getParameter("ckanDs");
			String ckanFilter = request.getParameter("ckanFilter");
			String ckanOffset = request.getParameter("ckanOffset");
			String typeDocWizard = (request.getParameter("typeDoc") != null && !"null".equals(request.getParameter("typeDoc"))) ? request
					.getParameter("typeDoc") : null;

			if (isTech != null && isTech.equals("true")) {
				// if is technical dataset == ENTERPRISE --> get all ADMIN/DEV public datasets
				dataSets = dataSetDao.loadEnterpriseDataSets();
			} else {
				if (allMyDataDS != null && allMyDataDS.equals("true")) {
					// get all the Datasets visible for the current user (MyData,Enterprise,Shared Datasets,Ckan)
					dataSets = dataSetDao.loadMyDataDataSets(((UserProfile) profile).getUserId().toString());
				} else if (ckanDS != null && ckanDS.equals("true")) {
					ckanJSONArray = getOnlineCkanDatasets(profile, ckanFilter, ckanOffset);
					dataSets = dataSetDao.loadCkanDataSets(((UserProfile) profile).getUserId().toString());
					synchronizeDatasets(dataSets, ckanJSONArray);
				} else {
					// else it is a custom dataset list --> get all datasets public with owner != user itself
					dataSets = dataSetDao.loadDatasetsSharedWithUser(((UserProfile) profile).getUserId().toString());
				}
			}
			logger.debug("Creating JSON...");
			long start = System.currentTimeMillis();
			datasetsJSONArray = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(dataSets, null);
			if (ckanDS != null && ckanDS.equals("true")) {
				if (ckanFilter.equals("NOFILTER") && ckanOffset.equals("0")) {
					for (int i = 0; i < ckanJSONArray.length(); i++) {
						datasetsJSONArray.put(ckanJSONArray.get(i));
					}
				} else { // Search by filter: list only unused CKAN datasets
					datasetsJSONArray = ckanJSONArray;
				}
			}

			JSONArray datasetsJSONReturn = putActions(profile, datasetsJSONArray, typeDocWizard);

			JSONReturn.put("root", datasetsJSONReturn);
			logger.debug("JSON created in " + (System.currentTimeMillis() - start) + "ms.");

		} catch (Throwable t) {
			throw new SpagoBIServiceException("An unexpected error occured while instatiating the dao", t);
		}
		return JSONReturn.toString();
	}

	private JSONArray putActions(IEngUserProfile profile, JSONArray datasetsJSONArray, String typeDocWizard) throws JSONException, EMFInternalError {

		Engine wsEngine = null;
		try {
			wsEngine = ExecuteAdHocUtility.getWorksheetEngine();
		} catch (SpagoBIRuntimeException r) {
			// the ws engine is not found
			logger.info("Engine not found. ", r);
		}

		Engine qbeEngine = null;
		try {
			qbeEngine = ExecuteAdHocUtility.getQbeEngine();
		} catch (SpagoBIRuntimeException r) {
			// the qbe engine is not found
			logger.info("Engine not found. ", r);
		}

		Engine geoEngine = null;
		try {
			geoEngine = ExecuteAdHocUtility.getGeoreportEngine();
		} catch (SpagoBIRuntimeException r) {
			// the geo engine is not found
			logger.info("Engine not found. ", r);
		}
		JSONObject detailAction = new JSONObject();
		detailAction.put("name", "detaildataset");
		detailAction.put("description", "Dataset detail");

		JSONObject deleteAction = new JSONObject();
		deleteAction.put("name", "delete");
		deleteAction.put("description", "Delete dataset");

		JSONObject worksheetAction = new JSONObject();
		worksheetAction.put("name", "worksheet");
		worksheetAction.put("description", "Show Worksheet");

		JSONObject georeportAction = new JSONObject();
		georeportAction.put("name", "georeport");
		georeportAction.put("description", "Show Map");

		JSONObject qbeAction = new JSONObject();
		qbeAction.put("name", "qbe");
		qbeAction.put("description", "Show Qbe");

		JSONObject infoAction = new JSONObject();
		infoAction.put("name", "info");
		infoAction.put("description", "Show Info");

		JSONArray datasetsJSONReturn = new JSONArray();
		for (int i = 0; i < datasetsJSONArray.length(); i++) {
			JSONArray actions = new JSONArray();
			JSONObject datasetJSON = datasetsJSONArray.getJSONObject(i);

			// Check if it is a CKAN dataset and if is already bookmarked
			if (datasetJSON.getString("dsTypeCd").equals("Ckan") && !datasetJSON.has("id")) {
				actions.put(infoAction);
			}

			if (typeDocWizard == null) {
				actions.put(detailAction);
				if (((UserProfile) profile).getUserId().toString().equals(datasetJSON.get("owner"))) {
					// the delete action is able only for private dataset
					actions.put(deleteAction);
				}
			}

			boolean isGeoDataset = false;
			if (!datasetJSON.getString("dsTypeCd").equals("Ckan")) { // GeoDatasets disabled for CKAN!
				try {
					String meta = datasetJSON.getString("meta");
					isGeoDataset = ExecuteAdHocUtility.hasGeoHierarchy(meta);
				} catch (Exception e) {
					logger.error("Error during check of Geo spatial column", e);
				}
			}
			if (isGeoDataset && geoEngine != null && typeDocWizard != null && typeDocWizard.equalsIgnoreCase("GEO")) {
				actions.put(georeportAction); // enable the icon to CREATE a new geo document
			} else {
				if (isGeoDataset && geoEngine != null) {
					// if (isGeoDataset && geoEngine != null &&
					// profile.getUserUniqueIdentifier().toString().equals(datasetJSON.get("owner"))){
					actions.put(georeportAction); // Annotated view map action to release SpagoBI 4
				}
			}
			if (wsEngine != null && typeDocWizard == null || typeDocWizard.equalsIgnoreCase("REPORT")) {
				actions.put(worksheetAction);

				if (qbeEngine != null && profile.getFunctionalities().contains(SpagoBIConstants.BUILD_QBE_QUERIES_FUNCTIONALITY)) {
					actions.put(qbeAction);
				}
			}

			datasetJSON.put("actions", actions);
			if (typeDocWizard != null && typeDocWizard.equalsIgnoreCase("GEO")) {
				// if is caming from myAnalysis - create Geo Document - must shows only ds geospatial --> isGeoDataset == true
				if (geoEngine != null && isGeoDataset)
					datasetsJSONReturn.put(datasetJSON);
			} else
				datasetsJSONReturn.put(datasetJSON);
		}
		return datasetsJSONReturn;
	}

	@GET
	@Path("/getflatdataset")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFlatDataSet(@Context HttpServletRequest req) {
		IDataSetDAO dataSetDao = null;
		List<IDataSet> dataSets;
		IEngUserProfile profile = (IEngUserProfile) req.getSession().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		JSONObject JSONReturn = new JSONObject();
		JSONArray datasetsJSONArray = new JSONArray();
		try {
			dataSetDao = DAOFactory.getDataSetDAO();
			dataSetDao.setUserProfile(profile);
			dataSets = dataSetDao.loadFlatDatasets();
			// dataSets = dataSetDao.loadFlatDatasets(profile.getUserUniqueIdentifier().toString());

			datasetsJSONArray = (JSONArray) SerializerFactory.getSerializer("application/json").serialize(dataSets, null);

			JSONArray datasetsJSONReturn = putActions(profile, datasetsJSONArray, null);

			JSONReturn.put("root", datasetsJSONReturn);

		} catch (Throwable t) {
			throw new SpagoBIServiceException("An unexpected error occured while instatiating the dao", t);
		}
		return JSONReturn.toString();
	}

	private JSONArray getOnlineCkanDatasets(IEngUserProfile profile, String filter, String offset) throws JSONException {

		JSONArray datasetsJsonArray = new JSONArray();
		Connection connection = null;

		boolean useIdM = CKANConfig.getInstance().getCkanIdMProperty();
		if (useIdM) {
			connection = new Connection(CKANConfig.getInstance().getCkanUrlProperty(), profile.getUserUniqueIdentifier().toString(), ((UserProfile) profile)
					.getUserId().toString());
		} else {
			connection = new Connection(CKANConfig.getInstance().getCkanUrlProperty(), null, null);
		}

		CKANClient client = new CKANClient(connection);
		try {
			logger.debug("Getting resources...");
			long start = System.currentTimeMillis();
			List<Resource> list = client.getAllResourcesCompatibleWithSpagoBI(filter, offset);
			logger.debug("Resources got in " + (System.currentTimeMillis() - start) + "ms.");
			logger.debug("Translating resources...");
			start = System.currentTimeMillis();
			for (Resource resource : list) {
				JSONObject jsonObj = CKANUtils.getJsonObjectFromCkanResource(resource);
				datasetsJsonArray.put(jsonObj);
			}
			logger.debug("Resources translated in " + (System.currentTimeMillis() - start) + "ms.");
		} catch (CKANException e) {
			logger.debug("Error while getting CKAN resources: " + e.getErrorMessages().get(0));
			throw new SpagoBIServiceException("REST service /certificateddatasets", "Error while getting CKAN resources: " + e.getErrorMessages().get(0));
		}
		return datasetsJsonArray;
	}

	private void synchronizeDatasets(List<IDataSet> spagobiDs, JSONArray ckanDs) throws JSONException {
		// boolean dsFound = false;
		logger.debug("Synchronize resources...");
		long start = System.currentTimeMillis();
		Iterator<IDataSet> iterator = spagobiDs.iterator();
		while (iterator.hasNext()) {
			IDataSet ds = iterator.next();
			String config = JSONUtils.escapeJsonString(ds.getConfiguration());
			JSONObject jsonConf = ObjectUtils.toJSONObject(config);
			for (int i = 0; i < ckanDs.length(); i++) {
				if (jsonConf.getString("ckanId").equals(ckanDs.getJSONObject(i).getJSONObject("configuration").getString("ckanId"))) {
					// dsFound = true;
					ckanDs.remove(i);
					break;
				}
			}
			// If the saved CKAN dataset is not available anymore, it has to be delete from Sbi... To be implemented
			// if (!dsFound) {
			// iterator.remove();
			// }
		}
		logger.debug("Resources synchronized in " + (System.currentTimeMillis() - start) + "ms.");
	}
}
