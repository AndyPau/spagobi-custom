/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  

/**
 * 
 * @author Marco Cortella (marco.cortella@eng.it)
 */

 Ext.define('Sbi.adhocreporting.MyAnalysisModel', {
    extend: 'Ext.data.Model',
    proxy:{
    	type : 'rest',
    	url : Sbi.config.serviceRegistry.getRestServiceUrl({
    		serviceName : 'documents/myAnalysisDocsList' 
    	}),
    	reader : {
    		type : 'json',
    		root : 'root'
    		
    	}
    },
    fields: 
    	[	    	 
    	 	"id",
    	 	"label",
    	 	"name",
    	 	"description",
    	 	"typeCode",
    	 	"typeId",
    	 	"encrypt",
    	 	"visible",
    	 	"engine",
    	 	"engineId",
    	 	"dataset",
    	 	"stateCode",
    	 	"stateId",
    	 	"functionalities",
    	 	"creationDate",
    	 	"creationUser",
    	 	"refreshSeconds",
    	 	"isPublic",
    	 	"actions",
    	 	"exporters",
    	 	"decorators",
    	 	"previewFile"

        ]
}); 