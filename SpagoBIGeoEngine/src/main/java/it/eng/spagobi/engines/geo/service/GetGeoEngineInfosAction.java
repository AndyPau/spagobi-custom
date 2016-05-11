/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.GeoEngine;


import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class GetGeoEngineInfosAction  extends AbstractGeoEngineAction {
	
	private static final String INFO_TYPE_PARAM_NAME = "infoType"; 
	private static final String INFO_TYPE_VERSION = "version"; 
	private static final String INFO_TYPE_NAME = "name"; 
	
	private static final long serialVersionUID = 1L;
	
	private static transient Logger logger = Logger.getLogger(GetGeoEngineInfosAction.class);
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) {	
		
		String infoType;
		String responseMessage;
		
		logger.debug("IN");
		
		try {	
				
			infoType = getAttributeAsString(INFO_TYPE_PARAM_NAME);
		
			if(INFO_TYPE_VERSION.equalsIgnoreCase( infoType )) {
				responseMessage = GeoEngine.getVersion().toString();
			} else if (INFO_TYPE_NAME.equalsIgnoreCase( infoType )) {
				responseMessage = GeoEngine.getVersion().getFullName();
			} else {
				responseMessage = GeoEngine.getVersion().getInfo();
			}
			
			
			tryToWriteBackToClient( responseMessage );
			
		} finally {
			logger.debug("OUT");
		}		
	}
}
	
