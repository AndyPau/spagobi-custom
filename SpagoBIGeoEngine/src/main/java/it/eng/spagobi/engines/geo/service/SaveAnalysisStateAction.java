/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.geo.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFAbstractError;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spagobi.commons.utilities.StringUtilities;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.engines.EngineAnalysisMetadata;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;


/**
 * The Class MapDrawAction.
 */
public class SaveAnalysisStateAction extends AbstractGeoEngineAction {
	
	// INPUT PARAMETERS
	public static final String ANALYSIS_NAME = "analysisName";	
	public static final String ANALYSIS_DESCRIPTION = "analysisDescription";
	public static final String ANALYSIS_SCOPE = "analysisScope";

	// RESPONSE PARAMETERS
	// ...
	
	// DEFAULT VALUES
	//...
	
	// Default serial version number (just to keep eclipse happy). 
	private static final long serialVersionUID = 1L;
	
	// Logger component
    public static transient Logger logger = Logger.getLogger(SaveAnalysisStateAction.class);

	public void service(SourceBean request, SourceBean response) {
		String analysisName = null;		
		String  analysisDescritpion  = null;		
		String  analysisScope  = null;
		EngineAnalysisMetadata analysisMetadata = null;
		String result = null;
		
		logger.debug("IN");
		
		try {
			super.service(request, response);		
			
			EMFErrorHandler errorHandler = getErrorHandler();
			if (!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
				Collection errors = errorHandler.getErrors();
				Iterator it = errors.iterator();
				while (it.hasNext()) {
					EMFAbstractError error = (EMFAbstractError) it.next();
					if (error.getSeverity().equals(EMFErrorSeverity.ERROR)) {
						throw new SpagoBIEngineServiceException(getActionName(), error.getMessage(), null);
					}
				}
			}
			
				
			
			analysisName = getAttributeAsString(ANALYSIS_NAME);		
			logger.debug(ANALYSIS_NAME + ": " + analysisName);
			analysisDescritpion  = getAttributeAsString(ANALYSIS_DESCRIPTION);
			logger.debug(ANALYSIS_DESCRIPTION + ": " + analysisDescritpion);
			analysisScope  = getAttributeAsString(ANALYSIS_SCOPE);
			logger.debug(ANALYSIS_SCOPE + ": " + analysisScope);
			
			
			Assert.assertNotNull(getEngineInstance(), "It's not possible to execute " + this.getActionName() + " service before having properly created an instance of EngineInstance class");
			Assert.assertTrue(!StringUtilities.isEmpty(analysisName), "Input parameter [" + ANALYSIS_NAME + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue(!StringUtilities.isEmpty(analysisDescritpion), "Input parameter [" + ANALYSIS_DESCRIPTION + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
			Assert.assertTrue(!StringUtilities.isEmpty(analysisScope), "Input parameter [" + ANALYSIS_SCOPE + "] cannot be null or empty in oder to execute " + this.getActionName() + " service");		
		
			analysisMetadata = getEngineInstance().getAnalysisMetadata();
			analysisMetadata.setName( analysisName );
			analysisMetadata.setDescription( analysisDescritpion );
		
			if( EngineAnalysisMetadata.PUBLIC_SCOPE.equalsIgnoreCase( analysisScope ) ) {
				analysisMetadata.setScope( EngineAnalysisMetadata.PUBLIC_SCOPE );
			} else if( EngineAnalysisMetadata.PRIVATE_SCOPE.equalsIgnoreCase( analysisScope ) ) {
				analysisMetadata.setScope( EngineAnalysisMetadata.PRIVATE_SCOPE );
			} else {
				Assert.assertUnreachable("Value [" + analysisScope + "] is not valid for the input parameter " + ANALYSIS_SCOPE);
			}
			
			
			result = saveAnalysisState();
			
			
			if(!result.trim().toLowerCase().startsWith("ok")) {
				throw new SpagoBIEngineServiceException(getActionName(), result);
			}
			
			try {
				String newSubobjectId = result.substring(5);
//				writeBackToClient( new JSONSuccess( result ) );
				writeBackToClient( new JSONSuccess( newSubobjectId ) );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}
			
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			// no resources need to be released
		}
		
		logger.debug("OUT");
	}
}