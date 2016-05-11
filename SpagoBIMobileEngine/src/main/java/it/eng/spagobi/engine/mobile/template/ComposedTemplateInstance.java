/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engine.mobile.template;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.commons.constants.ObjectsTreeConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.engine.mobile.MobileConstants;
import it.eng.spagobi.engines.config.bo.Engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComposedTemplateInstance extends AbstractTemplateInstance implements IMobileTemplateInstance{


	private static transient Logger logger = Logger.getLogger(ComposedTemplateInstance.class);

	private JSONObject documents = new JSONObject();
	private JSONObject features = new JSONObject();
	private JSONObject slider = new JSONObject();
	
	
	public ComposedTemplateInstance(SourceBean template, HashMap<String, String> paramsMap) {
		this.paramsMap = paramsMap;
		this.template = template;
	}

	public void loadTemplateFeatures() throws Exception {
		super.loadTemplateFeatures();
		buildDocumentsJSON();
		buildSliderJSON();
		setFeatures();
	}
	
	public void setFeatures() {
		try {
			features.put("documents", documents);
			features.put("slider", slider);

		} catch (JSONException e) {
			logger.error("Unable to set features");
		}		 
	}
	

	private void buildDocumentsJSON() throws Exception {

		logger.debug("IN");
		List docs = (List)template.getAttributeAsList(MobileConstants.DOCUMENTS_TAG+"."+MobileConstants.DOCUMENT_TAG);
		if(docs == null) {
			logger.warn("Cannot find columns configuration settings: tag name " + MobileConstants.DOCUMENTS_TAG+"."+MobileConstants.DOCUMENT_TAG);
		}else{
			//get total dimensions
			SourceBean docsTag = (SourceBean)template.getAttribute(MobileConstants.DOCUMENTS_TAG);
			String totWidth = (String)docsTag.getAttribute(MobileConstants.DOCUMENTS_WIDTH_ATTR);
			String totHeight = (String)docsTag.getAttribute(MobileConstants.DOCUMENTS_HEIGHT_ATTR);
			documents.put("totWidth", totWidth);
			documents.put("totHeight", totHeight);
			
			
			JSONArray docsArray = new JSONArray();
			for(int i=0; i<docs.size(); i++){
				SourceBean doc = (SourceBean)docs.get(i);
				JSONObject docJSON = new JSONObject();
				String label = (String)doc.getAttribute(MobileConstants.DOCUMENT_LABEL_ATTR);
				docJSON.put(ObjectsTreeConstants.OBJECT_LABEL, label);
				
				String width = (String)doc.getAttribute(MobileConstants.DOCUMENT_WIDTH_ATTR);
				docJSON.put("width", width);
	
				String height = (String)doc.getAttribute(MobileConstants.DOCUMENT_HEIGHT_ATTR);
				docJSON.put("height", height);
				//calls dao to get document type and id
				BIObject biDoc = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
				Engine engine = biDoc.getEngine();
				String engineName = engine.getName();
				docJSON.put(MobileConstants.ENGINE, engineName);
				docJSON.put(MobileConstants.TYPE_CODE, biDoc.getBiObjectTypeCode());
				Integer id = biDoc.getId();
				docJSON.put(ObjectsTreeConstants.OBJECT_ID, id);
				docsArray.put(docJSON);
				
				JSONObject inParameters = readInputParameters(doc, biDoc);
				docJSON.put(MobileConstants.IN_PARAMETERS, inParameters);
				
			}
			documents.put("docs", docsArray);
		}

		logger.debug("OUT");		

	}
	
	
	private JSONObject readInputParameters(SourceBean documentTemplate, BIObject biObject) throws Exception {
		JSONObject toReturn = new JSONObject();
		// reading biobject drivers
		List biParamenters = biObject.getBiObjectParameters();
		Iterator it = biParamenters.iterator();
		while (it.hasNext()) {
			BIObjectParameter aBiParameter = (BIObjectParameter) it.next();
			toReturn.put(aBiParameter.getParameterUrlName(), JSONObject.NULL);
		}
		
		// reading template for default values
		SourceBean inParameters = (SourceBean) documentTemplate.getAttribute(MobileConstants.IN_PARAMETERS);
		if (inParameters != null) {
			List inParametersList = inParameters.getAttributeAsList(MobileConstants.PARAMETER);
			if (inParametersList != null && inParametersList.size() > 0) {
				Iterator inParametersListIt = inParametersList.iterator();
				while (inParametersListIt.hasNext()) {
					SourceBean aInParameter = (SourceBean) inParametersListIt.next();
					String urlName = (String) aInParameter.getAttribute(MobileConstants.PARAMETER_URL_NAME);
					String defaultValue = (String) aInParameter.getAttribute(MobileConstants.PARAMETER_DEFAULT_VALUE);
					if (toReturn.has(urlName)) {
						logger.debug("Found default value for parameter [" + urlName + "]");
						toReturn.put(urlName, defaultValue);
					} else {
						logger.error("Template document contains a [" + urlName + "] parameter but the document hasn't");
					}
				}
			}
		}
		return toReturn;
	}
	protected void buildSliderJSON() throws Exception {
		
		SourceBean confSB = null;
		
		logger.debug("IN");
		confSB = (SourceBean)template.getAttribute(MobileConstants.SLIDER_TAG);
		if(confSB == null) {
			return;
		}else{

			String name = (String)confSB.getAttribute(MobileConstants.SLIDER_NAME_ATTR);
			String min = (String)confSB.getAttribute(MobileConstants.SLIDER_MIN_ATTR);
			String max = (String)confSB.getAttribute(MobileConstants.SLIDER_MAX_ATTR);
			String value =  (String)confSB.getAttribute(MobileConstants.SLIDER_VALUE_ATTR);
			String label = (String)confSB.getAttribute(MobileConstants.SLIDER_LABEL_ATTR);
			String increm = (String)confSB.getAttribute(MobileConstants.SLIDER_INCREMENT_ATTR);

			Map<String, String> params = getNotNullPrameters();
			if(params!=null && value!=null && value.length()!=0){
				value = StringUtilities.substituteParametersInString(value,params , null, false);
			}
			if(value!=null && value.length()==0){
				value = min;
			}
			
			slider.putOpt("name", name);
			slider.putOpt("value", value);
			slider.putOpt("minValue", min);
			slider.putOpt("maxValue", max);
			slider.putOpt("label", label);
			slider.putOpt("increment", increm);
		}
		
		logger.debug("OUT");		

	}
	@Override
	public JSONObject getFeatures() {
		return features;
	}

}
