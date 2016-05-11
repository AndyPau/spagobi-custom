/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
  
 
/**
  * Object name 
  * 
  * Singleton object that handle all errors generated on the client side
  * 
  * 
  * Public Properties
  * 
  * [list]
  * 
  * 
  * Public Methods
  * 
  *  [list]
  * 
  * 
  * Public Events
  * 
  *  [list]
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  * - Monica Franceschini (monica.franceschini@eng.it)
  * - Alberto Ghedin (alberto.ghedin@eng.it)
  */


Ext.ns("Sbi.exception.ExceptionHandler");

Sbi.exception.ExceptionHandler = function(){
	// do NOT access DOM from here; elements don't exist yet
 
    // private variables
 
    // public space
	return {
	
		init : function() {
			//alert("init");
		},
		
		
        handleFailure : function(response, options) {
        	
        	var errMessage = null;
        	if(response !== undefined) {        		
        		if(response.responseText !== undefined) {
        			var content = Ext.util.JSON.decode( response.responseText );
        			if(content.errors !== undefined && content.errors.length > 0) {
        				errMessage = '';
        				if (content.errors[0].message === 'session-expired') {
        					errMessage = 'Session Expired';
        				} else {
	        				for(var i = 0; i < content.errors.length; i++) {
	        					if(content.errors[i].message != '' && content.errors[i].message != undefined){
	        						errMessage += content.errors[i].message + '<br>';
	        					}
	        				}
        				}
        			} 
        		} 
        		if(errMessage === null)	errMessage = 'An unspecified error occurred on the server side';
        	} else {
        		errMessage = 'Request has been aborted due to a timeout trigger';
        	}
        		
        	errMessage = errMessage || 'An error occurred while processing the server error response';
        	
        	Sbi.exception.ExceptionHandler.showErrorMessage(errMessage, 'Service Error');
       	
        },

        
        
        showErrorMessage : function(errMessage, title) {
        	var m = errMessage || 'Generic error';
        	var t = title || 'Error';
        	
        	(Ext.Msg.alert('','<p style="color:#fff; font-weight: bold;">'+t+'</p><br/>'+m,Ext.emptyFn)).setHeight(250);
        },
        
        showWarningMessage : function(errMessage, title) {
        	var m = errMessage || 'Generic warning';
        	var t = title || 'Warning';
        	
        	(Ext.Msg.alert('','<p style="color:#fff; font-weight: bold;">'+t+'</p><br/>'+m,Ext.emptyFn)).setHeight(250);

        },
        
        showInfoMessage : function(errMessage, title) {
        	var m = errMessage || 'Info';
        	var t = title || 'Info';
        	
        	(Ext.Msg.alert('','<p style="color:#fff; font-weight: bold;">'+t+'</p><br/>'+m,Ext.emptyFn)).setHeight(250);
        }
        
        ,showConfirmMessage : function(errMessage, title, fn) {
        	var m = errMessage || '';
        	var t = title || 'Confirm';
        	
        	(Ext.Msg.confirm('','<p style="color:#fff; font-weight: bold;">'+t+'</p><br/>'+m,fn)).setHeight(250);
        }

	};
}();