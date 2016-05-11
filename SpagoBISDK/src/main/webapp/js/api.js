/** 
 * @license
 * SpagoBI, the Open Source Business Intelligence suite
 * 
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
Sbi.sdk.namespace('Sbi.sdk.api');

/**
* @namespace Sbi
*/

/**
* @namespace Sbi.sdk
*/

/** 
 * Note that Sbi.sdk.api definition is defined in both api.js and api_jsonp.js.
 * In api_jsonp.js there are functions that uses jsonp to avoid the same-origin policy.
 * The same functions were also developed with CORS and they are defined in api_cors.js.
 * 
 * jsonp is deprecated, it is highly recommended to use CORS instead of it.
 * 
 * NB: CORS functions are inside Sbi.sdk.cors.api namespace and have same names as jsonp counterpart.
 * @namespace Sbi.sdk.api
 */
Sbi.sdk.apply(Sbi.sdk.api, {
	
	elId: 0
	, dataSetList: {}

	, getIFrameHtml: function( serviceUrl, config ) {
		
		var html;
		config.iframe = config.iframe || {};
		
		if(config.iframe.id === undefined) {
			config.iframe.id = 'sbi-docexec-iframe-' + this.elId;
			this.elId = this.elId +1;
		}
		
		html = '';
		html += '<iframe';
		html += ' id = "' + config.iframe.id + '" ';
		html += ' src = "' + serviceUrl + '" ';
		if(config.iframe.style !== undefined) html += ' style = "' + config.iframe.style + '" ';
		if(config.iframe.width !== undefined) html += ' width = "' + config.iframe.width + '" ';
		if(config.iframe.height !== undefined) html += ' height = "' + config.iframe.height + '" ';
		html += '></iframe>';
		
		return html;
	}
	
	, injectIFrame: function( serviceUrl, config ) {
		
		var targetEl = config.target || document.body;
		
		if(typeof targetEl === 'string') {
			var elId = targetEl;
			targetEl = document.getElementById(targetEl);
			
			if(targetEl === null) {
				targetEl = document.createElement('div');
				targetEl.setAttribute('id', elId);
				if(config.width !== undefined) {
					targetEl.setAttribute('width', config.width);
				}				
				if(config.height !== undefined) {
					targetEl.setAttribute('height', config.height);
				}
				document.body.appendChild( targetEl );
			} 
		}
		
		config.iframe = config.iframe || {};
		config.iframe.width = targetEl.getAttribute('width');
		config.iframe.height = targetEl.getAttribute('height');
		
		
		targetEl.innerHTML = this.getIFrameHtml(serviceUrl, config);
	}

	, getDocumentUrl: function( config ) {
		var documentUrl = null;
		
		if(config.documentId === undefined && config.documentLabel === undefined) {
			alert('ERROR: at least one beetween documentId and documentLabel attributes must be specified');
			return null;
		}
		
		var params = Sbi.sdk.apply({}, config.parameters || {});
		
		if(config.documentId !== undefined) params.OBJECT_ID = config.documentId;
		if(config.documentLabel !== undefined) params.OBJECT_LABEL = config.documentLabel;
		
		if (config.executionRole !== undefined) params.ROLE = config.executionRole;
		if (config.displayToolbar !== undefined) params.TOOLBAR_VISIBLE = config.displayToolbar;
		if (config.theme !== undefined)	params.theme = config.theme;
		
		documentUrl = Sbi.sdk.services.getServiceUrl('execute', params);
		
		return documentUrl;
	}

	 /**
	 * It returns the HTML code of an iFrame containing document visualization. In particular config is an object that must contain at least one between documentId and documentLabel.
	 * It can also have (optional) parameters (an object containing values of document parameters), executionRole, displayToolbar and iframe, an object containing the style, height and width of the iframe where the document will be rendered (height and width can also be put outside the iframe object).
	 * @example
	 * var html = Sbi.sdk.api.getDocumentHtml({
	 * 		documentLabel: 'RPT_WAREHOUSE_PROF'
	 * 		, executionRole: '/spagobi/user'
	 * 		, parameters: {warehouse_id: 19}
	 * 		, displayToolbar: false
	 * 		, displaySliders: false
	 * 		, iframe: {
	 *     		height: '500px'
	 *     		, width: '100%'
	 * 			, style: 'border: 0px;'
	 * 		}
	 * 	});
	 * 
	 * @method Sbi.sdk.api.getDocumentHtml
	 * @param {Object} config - the configuration
	 * @param {String} config.documentId - the document id, must contain at least one between documentId and documentLabel
	 * @param {String} config.documentLabel - the document label,  must contain at least one between documentId and documentLabel
	 * @param {Object} [config.parameters] - the values of document parameters
	 * @param {String} [config.executionRole] - the role of execution
	 * @param {Boolean} [config.displayToolbar] - display or not the toolbar
	 * @param {Boolean} [config.displaySliders] - display or not the sliders
	 * @param {Object} [config.iframe] - the style object of iframe
	 * @param {String} [config.iframe.height] - the height of iframe
	 * @param {String} [config.iframe.width] - the width of iframe
	 * @param {String} [config.iframe.style] - the style of iframe
	 */
	, getDocumentHtml: function( config ) {
		
		var serviceUrl = this.getDocumentUrl( config );
		return this.getIFrameHtml(serviceUrl, config);
	}
	
	 /**
	 * It calls {@link Sbi.sdk.api.getDocumentHtml} and inject the generated iFrame inside a specified tag. If the target tag is not specified in config variable, it chooses the <body> tag as default.
	 * It can also have (optional) parameters (an object containing values of document parameters), executionRole, displayToolbar and iframe, an object containing the style, height and width of the iframe where the document will be rendered (height and width can also be put outside the iframe object).
	 * @see {@link Sbi.sdk.api.getDocumentHtml} 
	 * @example
	 * execTest8 = function() {
 	 *	Sbi.sdk.api.injectWorksheet({
	 *		datasetLabel: 'DS_DEMO_51_COCKPIT'
 	 *		, target: 'worksheet'
	 *		, height: '600px'
	 *		, width: '1100px'
	 *		, iframe: {
	 *			style: 'border: 0px;'
	 *		}
	 *	});
	 * };
	 *@method Sbi.sdk.api.injectDocument
	 * @param {Object} config - the configuration
	 * @param {String} config.documentId - the document id, must contain at least one between documentId and documentLabel
	 * @param {String} config.documentLabel - the document label,  must contain at least one between documentId and documentLabel
	 * @param {Object} [config.parameters] - the values of document parameters
	 * @param {String} [config.executionRole] - the role of execution
	 * @param {Boolean} [config.displayToolbar] - display or not the toolbar
	 * @param {Boolean} [config.displaySliders] - display or not the sliders
	 * @param {Object} [config.iframe] - the style object of iframe
	 * @param {String} [config.iframe.height] - the height of iframe
	 * @param {String} [config.iframe.width] - the width of iframe
	 * @param {String} [config.iframe.style] - the style of iframe
	 */
	, injectDocument: function( config ) {
		
		var serviceUrl = this.getDocumentUrl( config );
		return this.injectIFrame(serviceUrl, config);
	}
	
	, getAdHocReportingUrl: function( config ) {
		var url = null;
		
		if(config.datasetLabel === undefined) {
			alert('ERROR: datasetLabel attribute must be specified');
			return null;
		}
		
		var params = {};
		params.dataset_label = config.datasetLabel;
		params.TYPE_DOC = config.type;
		
		if (config.parameters !== undefined){
			for(var parameter in config.parameters)
				params[parameter] = config.parameters[parameter];
		}
		
		return Sbi.sdk.services.getServiceUrl('adHocReporting', params);
	}
	
	, getWorksheetUrl: function( config ) {
		config.type = 'WORKSHEET';
		return this.getAdHocReportingUrl(config);
	}
	
	/**
	 * It returns the HTML code of an iFrame containing worksheet visualization.
	 * config is an object that must contain datasetLabel and iframe, an object containing the style, height and width of the iframe where the worksheet and qbe will be rendered (height and width can also be put outside the iframe object).
	 * 
	 * @method Sbi.sdk.api.getWorksheetHtml
	 * @param {Object} config - the configuration
	 * @param {String} config.target - the target
	 * @param {String} config.documentLabel - the document label,  must contain at least one between documentId and documentLabel
	 * @param {String} [config.height] - the height of iframe, can be put inside iframe object
	 * @param {String} [config.width] - the width of iframe, can be put inside iframe object
	 * @param {Object} config.iframe - the style object of iframe
	 * @param {String} [config.iframe.height] - the height of iframe, can be put outside
	 * @param {String} [config.iframe.width] - the width of iframe
	 * @param {String} [config.iframe.style] - the style of iframe
	 */
	, getWorksheetHtml: function( config ) {
		
		var serviceUrl = this.getWorksheetUrl( config );
		return this.getIFrameHtml(serviceUrl, config);
	}
	
	/**
	 * It calls {@link Sbi.sdk.api.getWorksheetHtml} and inject the generated iFrame inside a specified tag. If the target tag is not specified in config variable, it chooses the <body> tag as default
	 * config is an object that must contain datasetLabel and iframe, an object containing the style, height and width of the iframe where the worksheet and qbe will be rendered (height and width can also be put outside the iframe object).
	 * 
	 * @method Sbi.sdk.api.injectWorksheet
	 * @param {Object} config - the configuration
	 * @param {String} [config.target="<body>"] - the target
	 * @param {String} config.documentLabel - the document label,  must contain at least one between documentId and documentLabel
	 * @param {String} [config.height] - the height of iframe, can be put inside iframe object
	 * @param {String} [config.width] - the width of iframe, can be put inside iframe object
	 * @param {Object} config.iframe - the style object of iframe
	 * @param {String} [config.iframe.height] - the height of iframe, can be put outside
	 * @param {String} [config.iframe.width] - the width of iframe
	 * @param {String} [config.iframe.style] - the style of iframe
	 */
	, injectWorksheet: function( config ) {
		
		var serviceUrl = this.getWorksheetUrl( config );
		return this.injectIFrame(serviceUrl, config);
	}
	
	, getQbeUrl: function( config ) {
		config.type = 'QBE';
		return this.getAdHocReportingUrl(config);
	}
	
	/**
	 * It returns the HTML code of an iFrame containing qbe visualization.
	 * config is an object that must contain datasetLabel and iframe, an object containing the style, height and width of the iframe where the worksheet and qbe will be rendered (height and width can also be put outside the iframe object).
	 * 
	 * @method Sbi.sdk.api.getQbeHtml
	 * @param {Object} config - the configuration
	 * @param {String} config.target - the target
	 * @param {String} config.documentLabel - the document label,  must contain at least one between documentId and documentLabel
	 * @param {String} [config.height] - the height of iframe, can be put inside iframe object
	 * @param {String} [config.width] - the width of iframe, can be put inside iframe object
	 * @param {Object} config.iframe - the style object of iframe
	 * @param {String} [config.iframe.height] - the height of iframe, can be put outside
	 * @param {String} [config.iframe.width] - the width of iframe
	 * @param {String} [config.iframe.style] - the style of iframe
	 */
	, getQbeHtml: function( config ) {
		
		var serviceUrl = this.getQbeUrl( config );
		return this.getIFrameHtml(serviceUrl, config);
	}
	
	/**
	 * It calls {@link Sbi.sdk.api.getQbeHtml} and inject the generated iFrame inside a specified tag. If the target tag is not specified in config variable, it chooses the <body> tag as default.
	 * config is an object that must contain datasetLabel and iframe, an object containing the style, height and width of the iframe where the worksheet and qbe will be rendered (height and width can also be put outside the iframe object).
	 * @example
	 *	execTest9 = function() {
	 *		Sbi.sdk.api.injectQbe({
	 *			datasetLabel: 'DS_DEMO_51_COCKPIT'
	 *			, target: 'qbe'
	 *			, height: '600px'
	 *			, width: '1100px'
	 *			, iframe: {
	 *			style: 'border: 0px;'
	 *		  }
	 *		});
	 *	};
	 * @method Sbi.sdk.api.injectQbe
	 * @param {Object} config - the configuration
	 * @param {String} [config.target="<body>"] - the target
	 * @param {String} config.documentLabel - the document label,  must contain at least one between documentId and documentLabel
	 * @param {String} [config.height] - the height of iframe, can be put inside iframe object
	 * @param {String} [config.width] - the width of iframe, can be put inside iframe object
	 * @param {Object} config.iframe - the style object of iframe
	 * @param {String} [config.iframe.height] - the height of iframe, can be put outside
	 * @param {String} [config.iframe.width] - the width of iframe
	 * @param {String} [config.iframe.style] - the style of iframe
	 */
	, injectQbe: function( config ) {
		
		var serviceUrl = this.getQbeUrl( config );
		return this.injectIFrame(serviceUrl, config);
	}
	
});