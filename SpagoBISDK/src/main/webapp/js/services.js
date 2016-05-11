/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
 
Sbi.sdk.namespace('Sbi.sdk.services');


Sbi.sdk.apply(Sbi.sdk.services, {

    services: null
    
    , baseUrl:  {
		protocol: 'http'     
		, host: 'localhost'
	    , port: '8080'
	    , contextPath: 'SpagoBI'
	    , controllerPath: 'servlet/AdapterHTTP'    
	}
    
    , initServices: function() {
        this.services = {};
        this.services.authenticate = {
            type: 'ACTION', 
            name: 'LOGIN_ACTION_WEB', 
            baseParams: {NEW_SESSION: 'TRUE'}
        };
        
        this.services.execute = {
            type: 'ACTION', 
            name: 'EXECUTE_DOCUMENT_ACTION', 
            baseParams: {NEW_SESSION: 'TRUE', IGNORE_SUBOBJECTS_VIEWPOINTS_SNAPSHOTS: 'true'}
        };
        
        this.services.adHocReporting = {
    		type: 'ACTION',
            name: 'AD_HOC_REPORTING_START_ACTION',
            baseParams: {NEW_SESSION: 'TRUE'}	
        };
    }
    
    , setBaseUrl: function(u) {
        Sbi.sdk.apply(this.baseUrl, u || {});
    }
    
    , getServiceUrl: function(serviceName, p) {
        var urlStr = null;
        
        if(this.services === null) {
            this.initServices();
        }
        
        if(this.services[serviceName] === undefined) {
            alert('ERROR: Service [' + serviceName + '] does not exist');
        } else {
            urlStr = '';
            urlStr = this.baseUrl.protocol + '://' + this.baseUrl.host + ":" + this.baseUrl.port + '/' + this.baseUrl.contextPath + '/' + this.baseUrl.controllerPath;
            var params;
            if(this.services[serviceName].type === 'PAGE'){
            	params = {PAGE: this.services[serviceName].name};
            } else {
            	params = {ACTION_NAME: this.services[serviceName].name};            	
            }
            
            Sbi.sdk.apply(params, p || {}, this.services[serviceName].baseParams || {});
            var paramsStr = Sbi.sdk.urlEncode(params);
            urlStr += '?' + paramsStr;
        }
        
        return urlStr;
    }
});

