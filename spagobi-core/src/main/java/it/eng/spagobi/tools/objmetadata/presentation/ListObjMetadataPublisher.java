/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.objmetadata.presentation;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.presentation.PublisherDispatcherIFace;
import it.eng.spagobi.commons.utilities.GeneralUtilities;

import org.apache.log4j.Logger;
/**
 * Publishes the results of a list information request for Metadgta
 * into the correct jsp page according to what contained into request. If Any errors occurred during the 
 * execution of the <code>ListObjMetadataModule</code> class, the publisher
 * is able to call the error page with the error message caught before and put into 
 * the error handler. If the input information doesn't fall into any of the cases declared,
 * another error is generated. 
 */
public class ListObjMetadataPublisher implements PublisherDispatcherIFace {
	static private Logger logger = Logger.getLogger(ListObjMetadataPublisher.class);
	
	/**
	 * Given the request at input, gets the name of the reference publisher,driving
	 * the execution into the correct jsp page, or jsp error page, if any error occurred.
	 * 
	 * @param requestContainer The object containing all request information
	 * @param responseContainer The object containing all response information
	 * 
	 * @return A string representing the name of the correct publisher, which will
	 * call the correct jsp reference.
	 */
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		logger.debug("IN");
		EMFErrorHandler errorHandler = responseContainer.getErrorHandler();
		
		// if there are errors and they are only validation errors return the name for the detail publisher
		if(!errorHandler.isOK()) {
			if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {				
				logger.info("Publish: listMetadata"  );
				logger.debug("OUT");
				return "listMetadata";
			}
		}
		
		if (errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)){
			logger.info("Publish: listObjMetadata"  );
			logger.debug("OUT");			
			return new String("listObjMetadata");
		}
		else {
			logger.info("Publish: error"  );
			logger.debug("OUT");
			return new String("error");
		}
	}

}
