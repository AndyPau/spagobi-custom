/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
  
 
/**
  * Object name 
  * 
  * [description]
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
  *  contentloaded: fired after the data has been loaded
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.formviewer");

Sbi.formviewer.DataStorePanel = function(config) {
	
	var defaultSettings = {
			displayInfo: true,
			pageSize: 25,
			sortable: false,
			sortMode: 'remote' // remote | local | auto
	};
			
	if(Sbi.settings && Sbi.settings.formviewer && Sbi.settings.formviewer.dataStorePanel) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.formviewer.resultsPage);
	}
	
	this.visibleselectfields = config.visibleselectfields;
	
	var c = Ext.apply(defaultSettings, config || {});
	
	Ext.apply(this, c);
	
	this.services = this.services || new Array();	
	this.services['loadDataStore'] = this.services['loadDataStore'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXECUTE_FORM_QUERY_ACTION'
		, baseParams: new Object()
	});
	
	this.services['exportDataStore'] = this.services['exportDataStore'] || Sbi.config.serviceRegistry.getServiceUrl({
		serviceName: 'EXPORT_RESULT_ACTION'
		, baseParams: new Object()
	});
	
	this.initStore(config);
	this.initPanel(config);
	this.addEvents('contentloaded');
	
	c = Ext.apply(c, {
		layout: 'fit',
		items: [this.grid]
	});
	

	
	// constructor
	Sbi.formviewer.DataStorePanel.superclass.constructor.call(this, c);
};

Ext.extend(Sbi.formviewer.DataStorePanel, Ext.Panel, {
    
    services: null
	, store: null
	, paging: null
	, pageSize: null
	, pageNumber: null
    
	// ---------------------------------------------------------------------------------------------------
    // public methods
	// ---------------------------------------------------------------------------------------------------
	
	, execQuery:  function(baseParams) {  
		this.store.removeAll();
		this.store.baseParams = baseParams;
		var requestParameters = {start: 0, limit: this.pageSize };
		this.store.load({params: requestParameters});
		this.doLayout();
	}

	, exportContent: function(params){
		var exportedTable = {PARS: params, SHEET_TYPE: 'TABLE'};
		return exportedTable;
	}

	, exportResult: function(mimeType) {
		var form = document.getElementById('export-form');
		if(!form) {
			var dh = Ext.DomHelper;
			form = dh.append(Ext.getBody(), {
			    id: 'export-form'
			    , tag: 'form'
			    , method: 'post'
			    , cls: 'export-form'
			});
		}
		
		form.action = this.services['exportDataStore'] + '&MIME_TYPE=' + mimeType +'&RESPONSE_TYPE=RESPONSE_TYPE_ATTACHMENT';
		form.submit();
	}
  	
	// ---------------------------------------------------------------------------------------------------
	// private methods
	// ---------------------------------------------------------------------------------------------------
	
	, initStore: function(config) {
		
		ExtendedStore = Ext.extend(Ext.data.Store, {

		    getQueryFn : null,
		    getQuery : function () {
		            var reader  = this.reader;
		            var getQuery = this.getQueryFn;
		            var rawData;

		        if (!getQuery) {
		        	getQuery = this.getQueryFn = reader.createAccessor('query');
		        }

		        return (function () {
		            rawData = reader.jsonData;

		            return getQuery(rawData);
		        })();
		    },
		    
		    getDataSourceFn : null,
		    getDataSource : function () {
	            var reader  = this.reader;
	            var getDataSource = this.getDataSourceFn;
	            var rawData;

	        if (!getDataSource) {
	        	getDataSource = this.getDataSourceFn = reader.createAccessor('dataSource');
	        }

	        return (function () {
	            rawData = reader.jsonData;

	            return getDataSource(rawData);
	        })();
	    }
		});
		
		var numberFormatterFunction;
		
		this.proxy = new Ext.data.HttpProxy({
	           url: this.services['loadDataStore']
	           , timeout : 300000
	   		   , failure: this.onDataStoreLoadException
	    });
		
		this.store = new ExtendedStore({
	        proxy: this.proxy,
	        reader: new Ext.data.JsonReader(),
	        remoteSort: true
	    });
		
//		this.store = new Ext.data.Store({
//	        proxy: this.proxy,
//	        reader: new Ext.data.JsonReader(),
//	        remoteSort: true
//	    });
		
		this.store.on('metachange', function( store, meta ) {
		   for(var i = 0; i < meta.fields.length; i++) {
			   if(meta.fields[i].type) {
				   var t = meta.fields[i].type;
				   //if(t === 'float' || t ==='int') t = 'number';
				   if (meta.fields[i].format) { // format is applied only to numbers
					   var format = Sbi.qbe.commons.Format.getFormatFromJavaPattern(meta.fields[i].format);
					   var formatDataSet = meta.fields[i].format;
					   if((typeof formatDataSet == "string") || (typeof formatDataSet == "String")){
						   try{
							   formatDataSet =  Ext.decode(meta.fields[i].format);
						   }catch(e){
							   formatDataSet = meta.fields[i].format;
						   }
					   }
					   var f = Ext.apply( {}, Sbi.locale.formats[t]);
					   f = Ext.apply( f, formatDataSet);

					   numberFormatterFunction = Sbi.qbe.commons.Format.numberRenderer(f);
				   } else {
					   numberFormatterFunction = Sbi.locale.formatters[t];
				   }	

				   
				   if (meta.fields[i].measureScaleFactor && (t === 'float' || t ==='int')) { // format is applied only to numbers
					  this.getScaleFunction(numberFormatterFunction,meta.fields[i]);
				   }else{
					   meta.fields[i].renderer = numberFormatterFunction;
				   }
			   }
	
			   if(meta.fields[i].subtype && meta.fields[i].subtype === 'html') {
				   meta.fields[i].renderer  =  Sbi.locale.formatters['html'];
			   }
			   if(meta.fields[i].subtype && meta.fields[i].subtype === 'timestamp') {
				   meta.fields[i].renderer  =  Sbi.locale.formatters['timestamp'];
			   }
			   
			   if(this.sortable === false) {
				   meta.fields[i].sortable = false;
			   } else {
				   if(meta.fields[i].sortable === undefined) { // keep server value if defined
					   meta.fields[i].sortable = true;
				   }
			   }
			   
		   }
		   meta.fields[0] = new Ext.grid.RowNumberer();
		   this.grid.getColumnModel().setConfig(meta.fields);

			//this.colorSegmentColumn();
			
		}, this);
		
		this.store.on('load', this.onDataStoreLoaded, this);
		
	}
	
	, getScaleFunction: function(numberFormatterFunction, field) {
		
		var scaleFactor = field.measureScaleFactor;
		
		if(scaleFactor!=null && scaleFactor!=null && scaleFactor!='NONE'){
			var scaleFactorNumber;
			switch (scaleFactor){
				case 'K':
					scaleFactorNumber=1000;
					break;
				case 'M':
					scaleFactorNumber=1000000;
					break;
				case 'G':
					scaleFactorNumber=1000000000;
					break;
				default:
					scaleFactorNumber=1;
			}
		
			field.renderer = function(v){
				 var scaledValue = v/scaleFactorNumber;
				 return numberFormatterFunction.call(this,scaledValue);	
			}
			
			field.header = field.header +' '+ LN('sbi.worksheet.config.options.measurepresentation.'+scaleFactor);
		}else{
			field.renderer =numberFormatterFunction;
		}
	}

	, initPanel: function(config) {
		var cm = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer(), 
			{
				header: "Data",
				dataIndex: 'data',
				width: 75
			}
		]);
		
		
		this.exportTBar = new Ext.Toolbar({
			items: [
			    new Ext.Toolbar.Button({
		            tooltip: LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' pdf',
		            iconCls:'pdf',
		            handler: this.exportResult.createDelegate(this, ['application/pdf']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' rtf',
		            iconCls:'rtf',
		            handler: this.exportResult.createDelegate(this, ['application/rtf']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' xls',
		            iconCls:'xls',
		            handler: this.exportResult.createDelegate(this, ['application/vnd.ms-excel']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' csv',
		            iconCls:'csv',
		            handler: this.exportResult.createDelegate(this, ['text/csv']),
		            scope: this
			    }),
			    new Ext.Toolbar.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' jrxml',
		            iconCls:'jrxml',
		            handler: this.exportResult.createDelegate(this, ['text/jrxml']),
		            scope: this
			    })
			]
		});
		
		this.warningMessageItem = new Ext.Toolbar.TextItem('<font color="red">' 
				+ LN('sbi.qbe.datastorepanel.grid.beforeoverflow') 
				+ ' [' + Sbi.config.queryLimit.maxRecords + '] '
				+ LN('sbi.qbe.datastorepanel.grid.afteroverflow') 
				+ '</font>');
		
		
		this.pagingTBar = new Ext.PagingToolbar({
            pageSize: this.pageSize,
            store: this.store,
            displayInfo: this.displayInfo,
            displayMsg: LN('sbi.qbe.datastorepanel.grid.displaymsg'),
            emptyMsg: LN('sbi.qbe.datastorepanel.grid.emptymsg'),
            beforePageText: LN('sbi.qbe.datastorepanel.grid.beforepagetext'),
            afterPageText: LN('sbi.qbe.datastorepanel.grid.afterpagetext'),
            firstText: LN('sbi.qbe.datastorepanel.grid.firsttext'),
            prevText: LN('sbi.qbe.datastorepanel.grid.prevtext'),
            nextText: LN('sbi.qbe.datastorepanel.grid.nexttext'),
            lastText: LN('sbi.qbe.datastorepanel.grid.lasttext'),
            refreshText: LN('sbi.qbe.datastorepanel.grid.refreshtext')
        });
		this.pagingTBar.on('render', function() {
			this.pagingTBar.addItem(this.warningMessageItem);
			this.warningMessageItem.setVisible(false);
			//this.pagingTBar.loading.setVisible(false); // it does not work with Ext 3.2.1
		}, this);
		
		var gridConf = {};
		if(config!=null && config.gridConfig!=null){
			gridConf = config.gridConfig;
		}
		
		// create the Grid
	    this.grid = new Ext.grid.GridPanel(Ext.apply({
	    	store: this.store,
	        cm: cm,
	        clicksToEdit:1,
	        style:'padding:10px',
	        frame: true,
	        border:true,
	        autoScroll: true,
	        collapsible: false,
	        loadMask: true,
	        viewConfig: {
	            forceFit:false,
	            autoFill: true,
	            enableRowBody:true,
	            showPreview:true
	        },
	        
	        //tbar:this.exportTBar,
	        bbar: this.pagingTBar
	    },gridConf));   
	}

	, onDataStoreLoaded: function(store) {
		 // GLOBAL VARIABLE TO LET ACCESS OTHER WEBAPPS TO SMART FILTER QUERY 
		 smartFilterQuery = store.getQuery();
		 smartFilterDataSource = store.getDataSource();
		 
		 this.fireEvent('contentloaded');
		 
		 var recordsNumber = store.getTotalCount();
       	 if(recordsNumber == 0) {
       		Ext.Msg.show({
				   title: LN('sbi.qbe.messagewin.info.title'),
				   msg: LN('sbi.qbe.datastorepanel.grid.emptywarningmsg'),
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.INFO,
				   modal: false
			});
       	 }
       	 
       	 if (Sbi.config.queryLimit.maxRecords !== undefined && recordsNumber > Sbi.config.queryLimit.maxRecords) {
       		if (Sbi.config.queryLimit.isBlocking) {
       			Sbi.exception.ExceptionHandler.showErrorMessage(this.warningMessageItem, LN('sbi.qbe.messagewin.error.title'));
       		} else {
       			this.warningMessageItem.show();
       		}
       	 } else {
       		this.warningMessageItem.hide();
       	 }
	}
	
	, onDataStoreLoadException: function(response, options) {
		this.fireEvent('contentloaded');
		Sbi.exception.ExceptionHandler.handleFailure(response, options);
	}
	
	//, colorSegmentColumn: function(){
	//	   // get sehgment column name
	//	  var segment = '';
	//	  var stop = false;
	//	   for(var j = 0; j < this.visibleselectfields.length && stop == false; j++) {
	//		   if(this.visibleselectfields[j].iconCls === 'segment_attribute'){
	//			   segment =  this.visibleselectfields[j].alias;
	//			   stop = true;
	//		   }
	//	   }
	//	   
	//	   stop = false;
	//	   
	//		var counter = this.grid.getColumnModel().getColumnCount(true);
	//		for(var i = 0; i < counter && stop == false; i++) {
	//			var column = this.grid.getColumnModel().getColumnById(i);
	//			if(column){
	//				if(column.header === segment){
	//					column.css = 'background-color:#00ff00;';
	//					stop = true;
	//				}
	//			}
	//		}
	//}
});