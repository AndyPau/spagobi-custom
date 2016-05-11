/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.api;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.AnalyticalModelDocumentManagementAPI;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.ObjectsAccessVerifier;
import it.eng.spagobi.services.serialization.JsonConverter;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.exceptions.SpagoBIServiceException;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
@Path("/1.0/documents")
public class DocumentResource extends AbstractSpagoBIResource {
	static protected Logger logger = Logger.getLogger(DocumentResource.class);

	protected AnalyticalModelDocumentManagementAPI documentManager = new AnalyticalModelDocumentManagementAPI(getUserProfile());

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response getDocuments(@QueryParam("inputType") String inputType) {
		logger.debug("IN");
		IBIObjectDAO documentsDao = null;
		List<BIObject> allObjects = null;
		List<BIObject> objects = null;

		try {
			documentsDao = DAOFactory.getBIObjectDAO();
			allObjects = documentsDao.loadAllBIObjects();

			UserProfile profile = getUserProfile();
			objects = new ArrayList<BIObject>();

			if (inputType != null && !inputType.isEmpty()) {
				for (BIObject obj : allObjects) {
					if (obj.getBiObjectTypeCode().equals(inputType) && ObjectsAccessVerifier.canSee(obj, profile))
						objects.add(obj);
				}
			} else {
				for (BIObject obj : allObjects) {
					if (ObjectsAccessVerifier.canSee(obj, profile))
						objects.add(obj);
				}
			}
			String toBeReturned = JsonConverter.objectToJson(objects, objects.getClass());

			return Response.ok(toBeReturned).build();
		} catch (Exception e) {
			logger.error("Error while getting the list of documents", e);
			throw new SpagoBIRuntimeException("Error while getting the list of documents", e);
		} finally {
			logger.debug("OUT");
		}
	}

	@POST
	@Path("/")
	@Consumes("application/json")
	public Response insertDocument(String body) {
		BIObject document = (BIObject) JsonConverter.jsonToValidObject(body, BIObject.class);

		document.setTenant(getUserProfile().getOrganization());
		document.setCreationUser((String) getUserProfile().getUserId());

		List<Integer> functionalities = document.getFunctionalities();
		for (Integer functionality : functionalities) {
			if (!ObjectsAccessVerifier.canDev(functionality, getUserProfile())) {
				String path = "";
				try {
					path = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(functionality, false).getPath();
				} catch (EMFUserError e) {
					// Do nothing, the correct SpagoBIRuntimeException will be throwed anyway. Only the path will be missing
				}

				throw new SpagoBIRuntimeException("User [" + getUserProfile().getUserName() + "] has no rights to create a document inside [" + path + "]");
			}
		}

		if (documentManager.isAnExistingDocument(document))
			throw new SpagoBIRuntimeException("The document already exists");

		documentManager.saveDocument(document, null);

		try {
			String encodedLabel = URLEncoder.encode(document.getLabel(), "UTF-8");
			encodedLabel = encodedLabel.replaceAll("\\+", "%20");
			return Response.created(new URI("1.0/documents/" + encodedLabel)).build();
		} catch (Exception e) {
			logger.error("Error while creating url of the new resource", e);
			throw new SpagoBIRuntimeException("Error while creating url of the new resource", e);
		}
	}

	@GET
	@Path("/{label}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response getDocumentDetails(@PathParam("label") String label) {
		BIObject document = documentManager.getDocument(label);
		if (document == null)
			throw new SpagoBIRuntimeException("Document with label [" + label + "] doesn't exist");

		try {
			if (ObjectsAccessVerifier.canSee(document, getUserProfile())) {
				String toBeReturned = JsonConverter.objectToJson(document, BIObject.class);
				return Response.ok(toBeReturned).build();
			} else
				throw new SpagoBIRuntimeException("User [" + getUserProfile().getUserName() + "] has no rights to see document with label [" + label + "]");

		} catch (SpagoBIRuntimeException e) {
			throw e;
		} catch (EMFInternalError e) {
			logger.error("Error while looking for authorizations", e);
			throw new SpagoBIRuntimeException("Error while looking for authorizations", e);
		} catch (Exception e) {
			logger.error("Error while converting document in Json", e);
			throw new SpagoBIRuntimeException("Error while converting document in Json", e);
		}

	}

	@PUT
	@Path("/{label}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response updateDocument(@PathParam("label") String label, String body) {
		BIObject oldDocument = documentManager.getDocument(label);
		if (oldDocument == null)
			throw new SpagoBIRuntimeException("Document with label [" + label + "] doesn't exist");

		Integer id = oldDocument.getId();
		if (!ObjectsAccessVerifier.canDevBIObject(id, getUserProfile()))
			throw new SpagoBIRuntimeException("User [" + getUserProfile().getUserName() + "] has no rights to update document with label [" + label + "]");

		BIObject document = (BIObject) JsonConverter.jsonToValidObject(body, BIObject.class);

		document.setLabel(label);
		document.setId(id);
		documentManager.saveDocument(document, null);
		return Response.ok().build();
	}

	@DELETE
	@Path("/{label}")
	public Response deleteDocument(@PathParam("label") String label) {
		BIObject document = documentManager.getDocument(label);
		if (document == null)
			throw new SpagoBIRuntimeException("Document with label [" + label + "] doesn't exist");

		if (ObjectsAccessVerifier.canDeleteBIObject(document.getId(), getUserProfile())) {
			try {
				DAOFactory.getBIObjectDAO().eraseBIObject(document, null);
			} catch (EMFUserError e) {
				logger.error("Error while deleting the specified document", e);
				throw new SpagoBIRuntimeException("Error while deleting the specified document", e);
			}
			return Response.ok().build();
		} else
			throw new SpagoBIRuntimeException("User [" + getUserProfile().getUserName() + "] has no rights to delete document with label [" + label + "]");
	}

	@GET
	@Path("/{label}/template")
	public Response getDocumentTemplate(@PathParam("label") String label) {
		BIObject document = documentManager.getDocument(label);
		if (document == null)
			throw new SpagoBIRuntimeException("Document with label [" + label + "] doesn't exist");

		if (!ObjectsAccessVerifier.canDevBIObject(document, getUserProfile()))
			throw new SpagoBIRuntimeException("User [" + getUserProfile().getUserName() + "] has no rights to see template of document with label [" + label
					+ "]");

		ResponseBuilder rb;
		ObjTemplate template = document.getActiveTemplate();

		// The template has not been found
		if (template == null)
			throw new SpagoBIRuntimeException("Document with label [" + label + "] doesn't contain a template");
		try {
			rb = Response.ok(template.getContent());
		} catch (Exception e) {
			logger.error("Error while getting document template", e);
			throw new SpagoBIRuntimeException("Error while getting document template", e);
		}

		rb.header("Content-Disposition", "attachment; filename=" + document.getActiveTemplate().getName());
		return rb.build();
	}

	// The file has to be put in a field called "file"
	@POST
	@Path("/{label}/template")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addDocumentTemplate(@PathParam("label") String label, MultipartFormDataInput input) {
		BIObject document = documentManager.getDocument(label);
		if (document == null)
			throw new SpagoBIRuntimeException("Document with label [" + label + "] doesn't exist");

		if (!ObjectsAccessVerifier.canDevBIObject(document, getUserProfile()))
			throw new SpagoBIRuntimeException("User [" + getUserProfile().getUserName() + "] has no rights to manage the template of document with label ["
					+ label + "]");

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		if (inputParts == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		for (InputPart inputPart : inputParts) {
			try {
				MultivaluedMap<String, String> header = inputPart.getHeaders();

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte[] content = IOUtils.toByteArray(inputStream);

				ObjTemplate template = new ObjTemplate();
				template.setContent(content);
				template.setName(getFileName(header));

				documentManager.saveDocument(document, template);

				return Response.ok().build();
			} catch (SpagoBIRuntimeException e) {
				throw e;
			} catch (Exception e) {
				logger.error("Error while getting the template", e);
				throw new SpagoBIRuntimeException("Error while getting the template", e);
			}
		}

		throw new SpagoBIRuntimeException("Template file not found inside the request");
	}

	@GET
	@Path("/{label}/meta")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public String getDocumentMeta(@PathParam("label") String label) {
		// TODO
		return null;
	}

	@GET
	@Path("/{label}/parameters")
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public String getDocumentParameters(@PathParam("label") String label) {
		logger.debug("IN");
		try {
			List<JSONObject> parameters = documentManager.getDocumentParameters(label);
			JSONArray paramsJSON = writeParameters(parameters);
			JSONObject resultsJSON = new JSONObject();
			resultsJSON.put("results", paramsJSON);
			return resultsJSON.toString();
		} catch (Throwable t) {
			throw new SpagoBIServiceException(this.request.getPathInfo(), "An unexpected error occured while executing service", t);
		} finally {
			logger.debug("OUT");
		}
	}

	public JSONArray writeParameters(List<JSONObject> params) throws Exception {
		JSONArray paramsJSON = new JSONArray();

		for (Iterator iterator = params.iterator(); iterator.hasNext();) {
			JSONObject jsonObject = (JSONObject) iterator.next();
			paramsJSON.put(jsonObject);
		}

		return paramsJSON;
	}

	// ===================================================================
	// UTILITY METHODS
	// ===================================================================

	private String getFileName(MultivaluedMap<String, String> multivaluedMap) {

		String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {

			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				return exactFileName;
			}
		}
		return "";
	}
}
