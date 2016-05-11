/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/

Ext.ns("Sbi.cockpit.widgets.table");

Sbi.cockpit.widgets.table.TableWidget = function(config) {
	Sbi.trace("[TableWidget.constructor]: IN");

	var defaultSettings = {
		displayInfo: false,
		pageSize: 50,
		sortable: true,
		//sortMode: 'local', // remote | local | auto
		layout: 'fit',
		timeout: 300000,
		split: true,
		collapsible: false,
		padding: '0 0 0 0',
		autoScroll: false,
		frame: false,
		border: false,
		gridConfig: {
			height: 400,
			clicksToEdit:1,
		    frame: false,
		    border:false,
		    autoScroll: true,
		    collapsible: false,
		    viewConfig: {
		    	forceFit:false,
		        autoFill: true,
		        enableRowBody:true,
		        showPreview:true,
		        loadMask: false
		    },
		    layout: "fit"
		},
		queryLimit: {
			maxRecords: 1000
			, isBlocking: false
		}
		, fieldsSelectionEnabled: true
	};

	var settings = Sbi.getObjectSettings('Sbi.cockpit.widgets.table.TableWidget', defaultSettings);
	var c = Ext.apply(settings, config || {});
	Ext.apply(this, c);

	this.initServices();
	this.init();

	this.addEvents('contentloaded');
	
	c = Ext.apply(c, {
		items: [this.grid]
	});


	Sbi.cockpit.widgets.table.TableWidget.superclass.constructor.call(this, c);

	this.on("afterRender", function(){
		this.reload();
		Sbi.trace("[TableWidget.onRender]: store loaded");
	}, this);

	this.addEvents('selection');

	Sbi.trace("[TableWidget.constructor]: OUT");
};

/**
 * @cfg {Object} config
 * ...
 */
Ext.extend(Sbi.cockpit.widgets.table.TableWidget, Sbi.cockpit.core.WidgetRuntime, {

	// =================================================================================================================
	// PROPERTIES
	// =================================================================================================================

	/**
     * @property {Array} services
     * This array contains all the services invoked by this class
     */
	services: null

	, grid: null
	, enablePaging: false
	, enableExport: false
	, fireSelectionEvent: true
	, aggregations : null

    // =================================================================================================================
	// METHODS
	// =================================================================================================================

    // -----------------------------------------------------------------------------------------------------------------
    // public methods
	// -----------------------------------------------------------------------------------------------------------------

	, boundStore: function() {
		Sbi.trace("[TableWidget.boundStore]: IN");
		Sbi.cockpit.widgets.table.TableWidget.superclass.boundStore.call(this);

		if(this.grid !== null) { // only if the grid has been already initialized reconfigure it properly
			Sbi.trace("[TableWidget.boundStore]: reconfiguring the grid...");
			var columns = this.initColumns();
			this.grid.reconfigure(this.getStore(), columns);
			Sbi.trace("[TableWidget.boundStore]: the grid has been reconfigured succesfully");
		} else {
			Sbi.trace("[TableWidget.boundStore]: the grid is not yet initialized.");
		}

		Sbi.trace("[TableWidget.boundStore]: OUT");
	}

	, refresh:  function() {
		Sbi.trace("[TableWidget.refresh]: IN");
		Sbi.cockpit.widgets.table.TableWidget.superclass.refresh.call(this);
		Sbi.trace("[TableWidget.refresh]: OUT");
	}

	, redraw: function() {
		Sbi.trace("[TableWidget.refresh]: IN");
		this.initFontOptions();		
		Sbi.cockpit.widgets.table.TableWidget.superclass.redraw.call(this);
		this.doLayout();
		Sbi.trace("[TableWidget.refresh]: OUT");
	}



	// -----------------------------------------------------------------------------------------------------------------
    // private methods
	// -----------------------------------------------------------------------------------------------------------------

	//------------------------------------------------------------------------------------------------------------------
	// utility methods
	// -----------------------------------------------------------------------------------------------------------------
	, onRender: function(ct, position) {
		Sbi.trace("[TableWidget.onRender][" + this.getId() + "]: IN");

		this.msg = 'Sono un widget di tipo TABLE';

		Sbi.cockpit.widgets.table.TableWidget.superclass.onRender.call(this, ct, position);

		Sbi.trace("[TableWidget.onRender][" + this.getId() + "]: OUT");
	}

	, onStoreMetaChange: function(store, meta) {
		Sbi.trace("[TableWidget.onStoreMetaChange][" + this.getId() + "]: IN");

		Sbi.cockpit.widgets.table.TableWidget.superclass.onStoreMetaChange.call(this, store, meta);

		var fields = new Array();

		var columns = [];

		for(var j = 0; j < this.wconf.visibleselectfields.length; j++) {
			for(var i = 0; i < meta.fields.length; i++) {
				
				var propToCheck;
				
				//attribute can appear once
				if(this.wconf.visibleselectfields[j].nature == 'attribute' || 
						this.wconf.visibleselectfields[j].funct === null ||
						this.wconf.visibleselectfields[j].funct == ''){
					propToCheck = this.wconf.visibleselectfields[j].columnName;
				}else{
					//measures can have the same field with different aggregation
					propToCheck = this.wconf.visibleselectfields[j].alias
				}
				
				
				if(meta.fields[i].header === propToCheck) {
//					if(this.wconf.visibleselectfields[j].funct != null &&
//							this.wconf.visibleselectfields[j].funct != 'NaN'
//								&& this.wconf.visibleselectfields[j].funct != ''){
//						meta.fields[i].header = this.wconf.visibleselectfields[j].funct+'('+this.wconf.visibleselectfields[j].alias+')';
//					}
					if(this.wconf.visibleselectfields[j].alias != null && 
							this.wconf.visibleselectfields[j].alias != ''){
						
						meta.fields[i].header = this.wconf.visibleselectfields[j].alias;
					}
					
					this.applyRendererOnField(meta.fields[i], this.wconf.visibleselectfields[j].funct);
					this.applySortableOnField(meta.fields[i]);

					if (this.wconf.visibleselectfields[j].width) {
						meta.fields[i].width = this.wconf.visibleselectfields[j].width;
					}

					fields.push(meta.fields[i]);
					columns.push(meta.fields[i].header);
					break;
				} else {
					Sbi.trace("[TableWidget.onStoreMetaChange]: field [" + this.wconf.visibleselectfields[j].id + "] is not equal to [" + meta.fields[i].header + "]");
				}
			}
		}
		Sbi.trace("[TableWidget.onStoreMetaChange]: visible fields are [" + columns.join(",") + "]");
		
		this.grid.reconfigure(this.getStore(), fields);		

		Sbi.trace("[TableWidget.onStoreMetaChange][" + this.getId() + "]: OUT");
	}

	, onDataChanged: function(store, eOpts) {
		Sbi.trace("[TableWidget.onDataChanged][" + this.getId() + "]: IN");
		this.fireSelectionEvent = false;
		this.grid.getView().refresh(); // see https://www.spagoworld.org/jira/browse/SPAGOBI-2135
		Sbi.trace("[TableWidget.onDataChanged][" + this.getId() + "]: OUT");
	}

	, onStoreLoad: function() {
		Sbi.trace("[TableWidget.onStoreLoad][" + this.getId() + "]: IN");
		Sbi.cockpit.widgets.table.TableWidget.superclass.onStoreLoad.call(this, this.getStore());
     	this.refreshWarningMessage();

		if (!this.areIncomingEventsEnabled()) {
			// in case this widget shouldn't receive any incoming event,
			// we unbind it from the store by cloning the store itself
			// and binding the grid to the clone
			var previousStore = this.getStore();
	     	var clone = Sbi.storeManager.cloneStore(previousStore);
	     	this.grid.reconfigure(clone);
	     	this.unboundStore();
	     	// we still need to know when something change on the original store!!
			// for example: when filters are removed.
			// We just refresh the grid view in order to highlight current selections
			previousStore.on('datachanged', function () {
				this.grid.getView().refresh();
			}, this);
		}
		if (this.wconf.maxRowsNumber !== undefined && 
				this.wconf.maxRowsNumber !== null && 
				this.wconf.maxRowsNumber !== '') {
	     	
	     	var maxRowsIndex = this.wconf.maxRowsNumber;
	     	var previousStore = this.getStore();
	     	var totalCount = previousStore.getTotalCount();
	     	
	     	if(previousStore.getTotalCount() > this.wconf.maxRowsNumber){
	     		
	     		var limitedRows = [];
	     		
	     		for(var k = 0; k < maxRowsIndex; k++){
	     			limitedRows.push(previousStore.inMemoryData[k]);
	     		}
	     		
		     	this.grid.getStore().loadData(limitedRows)
	     	}
		}
		
		if(this.rendered){
    		this.redraw();
    	} else {
    		this.on('afterrender', function(){this.redraw();}, this);
    	}
		
     	Sbi.trace("[TableWidget.onStoreLoad]: OUT");
	}

	, onAfterLayout: function() {
		Sbi.trace("[TableWidget.onAfterLayout][" + this.getId() + "]: IN");
		var selections = this.getWidgetManager().getWidgetSelections(this.getId());
		// TODO: reselect rows in a selective way
		this.fireSelectionEvent = true;
		
		Sbi.trace("[TableWidget.onAfterLayout][" + this.getId() + "]: OUT");
	}


	, refreshWarningMessage: function() {
		if(this.enablePaging === false) return;

		var recordsNumber = this.getStore().getTotalCount();

		if (this.queryLimit.maxRecords !== undefined && recordsNumber > this.queryLimit.maxRecords) {
     		if (this.queryLimit.isBlocking) {
     			Sbi.exception.ExceptionHandler.showErrorMessage(this.warningMessageItem, LN('sbi.qbe.messagewin.error.title'));
     		} else {
     			this.warningMessageItem.show();
     		}
     	} else {
     		this.warningMessageItem.hide();
     	}
	}

	, applyRendererOnField: function(field) {
		Sbi.trace("[TableWidget.applyRendererOnField]: IN");

		var rendererFunction = null;

		if(field.type) {
			var t = field.type;
			if (field.format) { // format is applied only to numbers
				var format = Sbi.commons.Format.getFormatFromJavaPattern(field.format);
				var formatDataSet = field.format;
				if((typeof formatDataSet == "string") || (typeof formatDataSet == "String")){
					try {
						formatDataSet =  Ext.decode(field.format);
					} catch(e) {
						formatDataSet = field.format;
					}
				}
				var f = Ext.apply( {}, Sbi.locale.formats[t]);
				f = Ext.apply( f, formatDataSet);

				numberFormatterFunction = Sbi.qbe.commons.Format.numberRenderer(f);
			} else {
				numberFormatterFunction = Sbi.locale.formatters[t];
			}

			if (field.measureScaleFactor && (t === 'float' || t ==='int')) { // format is applied only to numbers
				rendererFunction = this.applyScaleRendererOnField(numberFormatterFunction, field);
			} else {
				rendererFunction = numberFormatterFunction;
			}
		}

		if(field.subtype && field.subtype === 'html') {
			rendererFunction = Sbi.locale.formatters['html'];
		}

		if(field.subtype && field.subtype === 'timestamp') {
			rendererFunction = Sbi.locale.formatters['timestamp'];
		}

		// the following renderer will apply a style to previously selected cells
		var applyCellStyleRenderer = function (value, metadata, record, rowIndex, colIndex, store, view, fieldHeader) {
			// optimization: we could retrieve the current selections by
			// this.getWidgetManager().getWidgetSelections(this.getId()) || {};
			// but this takes a long time!!! and therefore the rendering of the grid
			// takes a long time because it is evaluated for all cells!!!
			// Solution: we put selections on this.selectionsForColumnRenderers variable when
			// the grid's 'beforerefresh' event is fired (see setSelectionsForColumnRenderers method)
			// in a way that this.getWidgetManager().getWidgetSelections(this.getId()) || {};
			// is evaluated one time for all the cells
			var selections = this.selectionsForColumnRenderers;
			
	    	for(var j = 0; j < this.wconf.visibleselectfields.length; j++) {
				if(fieldHeader === this.wconf.visibleselectfields[j].alias) {				
					fieldHeader = this.wconf.visibleselectfields[j].columnName;
				}
	    	}
			
	    	if (selections[fieldHeader] !== undefined && selections[fieldHeader].values.indexOf(value) != -1) {
	    		metadata.attr = 'style="background-color: #D1D1D1;font-weight: bold;"';
	    	}
			return value;
		};

		field.renderer = Ext.Function.createSequence(rendererFunction, Ext.bind(applyCellStyleRenderer, this, [field.header], true));
		field.scope = this;

		Sbi.trace("[TableWidget.applyRendererOnField]: OUT");
	}

	, applyScaleRendererOnField: function(numberFormatterFunction, field) {

		Sbi.trace("[TableWidget.applyScaleRendererOnField]: IN");

		var toReturn = null;

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

			toReturn = function(v) {
				 var scaledValue = v/scaleFactorNumber;
				 return numberFormatterFunction.call(this, scaledValue);
			};

			field.header = field.header +' '+ LN('sbi.worksheet.config.options.measurepresentation.'+scaleFactor);
		} else {
			toReturn = numberFormatterFunction;
		}

		Sbi.trace("[TableWidget.applyScaleRendererOnField]: OUT");

		return toReturn;
	}

	, applySortableOnField: function(field) {
		Sbi.trace("[TableWidget.applySortableOnField]: IN");
		if(this.sortable === false) {
		   field.sortable = false;
		} else {
		   if(field.sortable === undefined) { // keep server value if defined
			   field.sortable = true;
		   }
		}
		Sbi.trace("[TableWidget.applySortableOnField]: OUT");
	}


   // -----------------------------------------------------------------------------------------------------------------
   // init methods
   // -----------------------------------------------------------------------------------------------------------------

	/**
	 * @method
	 *
	 * Initialize the following services exploited by this component:
	 *
	 *    - none
	 */
	, initServices: function() {
		this.services = this.services || new Array();
	}

	/**
	 * @method
	 *
	 * Initialize the GUI
	 */
	, init: function() {
		Sbi.trace("[TableWidget.init]: IN");


		if(this.wconf.series || this.wconf.category){
			this.aggregations = {};
//			this.aggregations.series = this.wconf.series;
//			this.aggregations.category = this.wconf.category;

			var categories = [];
			if(this.wconf.category instanceof Array){
				for(var i = 0; i<this.wconf.category.length;i++){
					var cat = this.wconf.category[i];
					categories.push(cat);
				}
			}
			else{
				categories.push(wizardState.wconf.category);
			}

			//if(wizardState.wconf.groupingVariable) categories.push(wizardState.wconf.groupingVariable);

			this.aggregations.measures = this.wconf.series;
			this.aggregations.categories = categories;

		}

		this.boundStore();
		this.initGridPanel();
		Sbi.trace("[TableWidget.init]: OUT");
	}

	/**
	 * @method
	 *
	 * Initialize the grid
	 */
	, initGridPanel: function() {
		Sbi.trace("[TableWidget.initGridPanel]: IN");

		var columns = this.initColumns();

		var gridConf = {
			store: this.getStore()
		    , columns: columns
//		    sm : new Ext.grid.RowSelectionModel( {
//				singleSelect : true
//			})
//		    selModel: {selType: 'cellmodel', mode: 'SINGLE', allowDeselect: true}
		};
		if(this.enableExport === true) {
			this.initExportToolbar();
			gridConf.tbar=this.exportTBar;
		}
		if(this.enablePaging === true) {
			this.initFilteringToolbar();
			gridConf.bbar=this.pagingTBar;
		}
		if(this.gridConfig!=null){
			gridConf = Ext.apply(gridConf, this.gridConfig);
		}

		if (this.areOutcomingEventsEnabled()) {
			gridConf.cls = "tableWidget";  // this highlight the cells to be selected
		}

		gridConf.pagingConfig = {};

		// create the Grid
	    //this.grid = new Ext.grid.GridPanel(gridConf);
		this.grid = new Sbi.widgets.grid.InMemoryPagingGridPanel(gridConf);
	    //this.grid.on('selectionchange', this.onSelectionChange, this);
	    this.grid.on('cellclick', this.onCellclick, this);
	    this.grid.on('columnresize', this.onColumnResize, this);
	    this.grid.on('columnmove', this.onColumnMove, this);
	    this.grid.on('afterlayout', this.onAfterLayout, this);
	    // optimization: this is useful for columns rendering (see applyCellStyleRenderer function)
	    this.grid.getView().on('beforerefresh', this.setSelectionsForColumnRenderers, this);	
	    
	    Sbi.trace("[TableWidget.initGridPanel]: OUT");
	}

	,
	// optimization: this is useful for columns rendering (see applyCellStyleRenderer function)
	setSelectionsForColumnRenderers : function () {
		this.selectionsForColumnRenderers = this.getWidgetManager().getWidgetSelections(this.getId()) || {};
		return true;
	}

	, onColumnResize: function (ct, column, width, eOpts){
		this.wconf.visibleselectfields[column.getIndex()].width = column.width;
	}

	, onColumnMove: function (ct, column, fromIdx, toIdx, eOpts){
		Sbi.trace("[TableWidget.onColumnMove]: IN");

		Sbi.trace("[TableWidget.onColumnMove]: fromIdx= " + fromIdx + " - toIdx= " + toIdx);

		var toIndex = toIdx;

		/*
		 * Moving a column forward, columnresize method count also the moving column itself
		 * so the right toIndex base 0 is (toIdx - 1)
		 */
		if (fromIdx < toIdx){
			toIndex = toIdx - 1;
		}

		var columnArray = this.wconf.visibleselectfields;
		var mixedArray = [];

		Sbi.trace("[TableWidget.onColumnMove]: ColumnArray " + Sbi.toSource(columnArray));

		Ext.each(columnArray, function (val,index){
			if (index == toIndex){
				/* Perform the move of the selected column */
				Sbi.trace("[TableWidget.onColumnMove]: index(" + index + ") equals toIdx - pushing " + columnArray[fromIdx].id);

				mixedArray.push(columnArray[fromIdx]);

			} else if (index == fromIdx) {
				Sbi.trace("[TableWidget.onColumnMove]: index(" + index + ") equals fromIdx");

				if (fromIdx > toIdx){
					/* Column have been pushed to the right */
					Sbi.trace("[TableWidget.onColumnMove]: fromIdx > toIdx - pushing " + columnArray[index - 1].id);
					mixedArray.push(columnArray[index - 1]);
				} else {
					/* Column have been pushed to the left */
					Sbi.trace("[TableWidget.onColumnMove]: fromIdx < toIdx - pushing " + columnArray[index + 1].id);
					mixedArray.push(columnArray[index + 1]);
				}

			} else {
				if ((index > toIndex) && (index < fromIdx)){
					/* Column between a move from right to left */
					Sbi.trace("[TableWidget.onColumnMove]: " + index + "=" + index + " ( index > toIdx) - pushing " + columnArray[index - 1].id);
					mixedArray.push(columnArray[index - 1]);
				} else if ((index < toIndex) && (index > fromIdx)){
					/* Column between a move from left to right */
					Sbi.trace("[TableWidget.onColumnMove]: " + index + "=" + index + " ( index < toIdx) - pushing " + columnArray[index + 1].id);
					mixedArray.push(columnArray[index + 1]);
				} else {
					/* Column not influenced by the move */
					Sbi.trace("[TableWidget.onColumnMove]: " + index + "=" + index + " - pushing " + columnArray[index].id);
					mixedArray.push(columnArray[index]);
				}
			}
		});

		this.wconf.visibleselectfields = mixedArray;

		Sbi.trace("[TableWidget.onColumnMove]: MixedArray " + Sbi.toSource(mixedArray));

		Sbi.trace("[TableWidget.onColumnMove]: OUT");
	}

	,
	onCellclick : function ( thisGrid, td, cellIndex, record, tr, rowIndex, e, eOpts ) {

		if (this.fireSelectionEvent === false || !this.areOutcomingEventsEnabled()) {
			//alert("onSelectionChange disabled");
			return;
		} else {
			//alert("onSelectionChange enabled");
		}

		var selections = {};
		// get new selection
		var selection = this.extractSelectionsFromRecord(cellIndex, record);
		if (selection != null) { // selection may be null, see extractSelectionsFromRecord
			var fieldHeader = selection.header;
			var value = selection.value;
			//selections[fieldHeader] = selections[fieldHeader] || {values: []};
			selections[fieldHeader] = {values: []};
			Ext.Array.include(selections[fieldHeader].values, value);

			this.fireEvent('selection', this, selections);

			if (!this.areIncomingEventsEnabled()) {
				// we need to refresh the grid in order to highlight current selections
				this.grid.getView().refresh();
			}
		}

	}

	,
	extractSelectionsFromRecord : function (cellIndex, record) {
    	var selection = {};

    	var meta = Sbi.storeManager.getRecordMeta(record);

    	var header = this.grid.getView().getHeaderCt().getHeaderAtIndex(cellIndex);
    	var fieldName = header.dataIndex;
    	var fieldHeader = header.text;
    	
    	//Added after table alias configuration, to manage the different header
    	for(var j = 0; j < this.wconf.visibleselectfields.length; j++) {
			if(fieldHeader === this.wconf.visibleselectfields[j].alias) {				
				fieldHeader = this.wconf.visibleselectfields[j].columnName;
			}
    	}

    	if (!meta[fieldHeader]) {
			Sbi.error("[TableWidget.extractSelectionsFromRecord]: column with header [" + fieldHeader + "] not found on record's metadata");
			return null;
    	}

		if(meta[fieldHeader].type.type === 'float') {
			// Ignoriamo le colonne di tipo float perchï¿½ applicando un filtro di uguaglianza su di esse
			// in alcuni database (ex. mysql)non si hanno risultati per via di errori di approssimmazione
			// @see http://stackoverflow.com/questions/5921584/cannot-achieve-a-where-clause-on-a-float-value
			// TODO possibile soluzione pulita: quando si persiste la tabella in cache per i database problematici
			// evitare di usare il tipo float. Usare solo decimal con precisione fissata. Il numero reale dovrebbe
			// poi essere arrotondato a tale precisione.
			Sbi.warn("[TableWidget.extractSelectionsFromRecord]: column [" + fieldHeader + "] is of type [float] so its selection will be ignored");
			return null;
		}
		var fieldValue = record.data[fieldName];

		selection.header = fieldHeader;//header.text;
		selection.value = fieldValue;

    	return selection;
	}


	/*
	, onSelectionChange: function( sm,selected,opt){

		if(this.fireSelectionEvent === false) {
			//alert("onSelectionChange disabled");
			return;
		} else {
			//alert("onSelectionChange enabled");
		}
        var records = sm.getSelection();

        var selections = {};

        for (var i=0; i< records.length; i++){
    		var s = this.extractSelectionsFromRecord(records[i]);
    		for(var fieldHeader in s) {
    			selections[fieldHeader] = selections[fieldHeader] || {values: []};
    			// Push the selected value into the selections only if the selection doesn't contain it yet
    			Ext.Array.include(selections[fieldHeader].values, s[fieldHeader]);
    		}
        }
		this.fireEvent('selection', this, selections);
	}
	*/

	/*
	, extractSelectionsFromRecord: function(record) {
    	var selections = {};

    	var meta = Sbi.storeManager.getRecordMeta(record);
    	//alert(Sbi.toSource(meta));

    	var fields = record.data;

    	for (fieldName in fields){
    		if (fieldName === 'id' || fieldName === 'recNo') continue;

    		var fieldHeader = Sbi.storeManager.getFieldHeaderByName(meta, fieldName);
    		//alert(fieldHeader + " = " + meta[fieldHeader].type.type + " - " + (meta[fieldHeader].type.type === 'float'));
    		if(meta[fieldHeader].type.type === 'float') {
    			// Ignoriamo le colonne di tipo float perchï¿½ applicando un filtro di uguaglianza su di esse
    			// in alcuni database (ex. mysql)non si hanno risultati per via di errori di approssimmazione
    			// @see http://stackoverflow.com/questions/5921584/cannot-achieve-a-where-clause-on-a-float-value
    			// TODO possibile soluzione pulita: quando si persiste la tabella in cache per i database problematici
    			// evitare di usare il tipo float. Usare solo decimal con precisione fissata. Il numero reale dovrebbe
    			// poi essere arrotondato a tale precisione.
    			Sbi.warn("[TableWidget.onColumnMove]: column [" + fieldHeader + "] is of type [float] so its selction will be ignored");
    			continue;
    		}
    		var fieldValue = fields[fieldName];

    		selections[fieldHeader] = fieldValue;
    	}

    	return selections;
    }
    */

	, initColumns: function() {
		var columns = [
			//new Ext.grid.RowNumberer(),
			{
				header: "Data",
	   			dataIndex: 'data',
	   			width: 75
	   		}
		];
		return columns;
	}

	, initExportToolbar: function() {
		this.exportTBar = new Ext.Toolbar({
			items: [
			    new Ext.Button({
		            tooltip: LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' pdf',
		            iconCls:'pdf',
		            //handler: this.exportResult.createDelegate(this, ['application/pdf']),
		            handler: function(){Ext.Msg.alert('Message', 'Export to pdf');},
		            scope: this
			    }),
			    new Ext.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' rtf',
		            iconCls:'rtf',
		            //handler: this.exportResult.createDelegate(this, ['application/rtf']),
		            handler: function(){Ext.Msg.alert('Message', 'Export to rtf');},
		            scope: this
			    }),
			    new Ext.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' xls',
		            iconCls:'xls',
		            //handler: this.exportResult.createDelegate(this, ['application/vnd.ms-excel']),
		            handler: function(){Ext.Msg.alert('Message', 'Export to xls');},
		            scope: this
			    }),
			    new Ext.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' csv',
		            iconCls:'csv',
		            //handler: this.exportResult.createDelegate(this, ['text/csv']),
		            handler: function(){Ext.Msg.alert('Message', 'Export to csv');},
		            scope: this
			    }),
			    new Ext.Button({
		            tooltip:LN('sbi.qbe.datastorepanel.button.tt.exportto') + ' jrxml',
		            iconCls:'jrxml',
		            //handler: this.exportResult.createDelegate(this, ['text/jrxml']),
		            handler: function(){Ext.Msg.alert('Message', 'Export to jrxml');},
		            scope: this
			    })
			]
		});
		return this.exportTBar;
	}

	, initFilteringToolbar: function() {
		this.warningMessageItem = new Ext.Toolbar.TextItem('<font color="red">'
				+ LN('sbi.qbe.datastorepanel.grid.beforeoverflow')
				+ ' [' + this.queryLimit.maxRecords + '] '
				+ LN('sbi.qbe.datastorepanel.grid.afteroverflow')
				+ '</font>');


		this.pagingTBar = new Ext.PagingToolbar({
            pageSize: this.pageSize,
//            store: this.getStore(),
            store: this.grid.getStore(),
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
		}, this);

		return this.pagingTBar;
	}
	
	//grid font setting
	, initFontOptions: function() {
		
		//font options		
	    if(this.wconf === undefined || this.wconf === null){
	    	//do not change the CSS, do nothing
	    }
	    else{
	    	
	    	//font options
		    var headerFontStyle = '#' + this.grid.id + ' .x-column-header-text { font: ';
		    var rowsFontStyle = '#' + this.grid.id + ' .x-grid-cell { font: ';
    	
		    //header font weight
		    if(this.wconf.headerFontWeight === undefined || this.wconf.headerFontWeight  === null){
				headerFontStyle = headerFontStyle + 'normal ';	
			} else {
				headerFontStyle = headerFontStyle + this.wconf.headerFontWeight + ' ';
			}
		    
		    //rows font weight
		    if(this.wconf.rowsFontWeight === undefined || this.wconf.rowsFontWeight  === null){
				rowsFontStyle = rowsFontStyle + 'normal ';	
			} else {
				rowsFontStyle = rowsFontStyle + this.wconf.rowsFontWeight + ' ';
			}
	    
		 	//header font size
		    if(this.wconf.headerFontSize === undefined || this.wconf.headerFontSize === null){
	    		
				if (this.wconf.fontSize == undefined || this.wconf.fontSize == null){
					headerFontStyle = headerFontStyle + '11px/13px ';
				} else {
					headerFontStyle = headerFontStyle + this.wconf.fontSize + 'px ';
				}			
			} else {
				headerFontStyle = headerFontStyle + this.wconf.headerFontSize + 'px ';
			}
	    	
	    	//rows font size
	    	if(this.wconf.rowsFontSize === undefined || this.wconf.rowsFontSize === null){
	    		
				if (this.wconf.fontSize == undefined || this.wconf.fontSize == null){
					rowsFontStyle = rowsFontStyle + '11px/13px ';
				} else {
					rowsFontStyle = rowsFontStyle + this.wconf.fontSize + 'px ';
				}			
			} else {
				rowsFontStyle = rowsFontStyle + this.wconf.rowsFontSize + 'px ';
			}
		    
		    //font family
	    	if (this.wconf.fontType === undefined || this.wconf.fontType === null){
				headerFontStyle = headerFontStyle + 'tahoma,arial,verdana,sans-serif; ';
				rowsFontStyle = rowsFontStyle + 'tahoma,arial,verdana,sans-serif; ';
			} else {
				headerFontStyle = headerFontStyle + '' + this.wconf.fontType + '; ';
				rowsFontStyle = rowsFontStyle  + '' + this.wconf.fontType + '; ';
			}
	    	
	    	//font decoration
	    	//header font decoration
		    if(this.wconf.headerFontDecoration === undefined || this.wconf.headerFontDecoration  === null){
				headerFontStyle = headerFontStyle + 'text-decoration: none; ';	
			} else {
				headerFontStyle = headerFontStyle + 'text-decoration: ' + this.wconf.headerFontDecoration + '; ';
			}
		    
			//rows font decoration
		    if(this.wconf.rowsFontDecoration === undefined || this.wconf.rowsFontDecoration  === null){
				rowsFontStyle = rowsFontStyle + 'text-decoration: none; ';	
			} else {
				rowsFontStyle = rowsFontStyle + 'text-decoration: ' + this.wconf.rowsFontDecoration + '; ';
			}
		    
		    //font color
		    //header font color
		    if(this.wconf.headerFontColor === undefined || this.wconf.headerFontColor === null || this.wconf.headerFontColor === ''){
				headerFontStyle = headerFontStyle + '} ';	
			} else {
				headerFontStyle = headerFontStyle + 'color: ' + this.wconf.headerFontColor + '; } ';
			}
		    
			//rows font color
		    if(this.wconf.rowsFontColor === undefined || this.wconf.rowsFontColor === null || this.wconf.rowsFontColor === ''){
		    	rowsFontStyle = rowsFontStyle + '} ';	
			} else {
				rowsFontStyle = rowsFontStyle + 'color: ' + this.wconf.rowsFontColor + '; } ';
			}

		    if(Ext.util.CSS.getRule(headerFontStyle) !== undefined || Ext.util.CSS.getRule(headerFontStyle) !== null){
		    	Ext.util.CSS.removeStyleSheet(this.grid.id + '_hstyle');
		    	Ext.util.CSS.createStyleSheet(headerFontStyle, this.grid.id + '_hstyle');
		    }
		    else {
		    	Ext.util.CSS.createStyleSheet(headerFontStyle, this.grid.id + '_hstyle');
		    }
		    
		    if(Ext.util.CSS.getRule(rowsFontStyle) !== undefined || Ext.util.CSS.getRule(rowsFontStyle) !== null){
		    	Ext.util.CSS.removeStyleSheet(this.grid.id + '_rstyle');
		    	Ext.util.CSS.createStyleSheet(rowsFontStyle, this.grid.id + '_rstyle');
		    }
		    else {
		    	Ext.util.CSS.createStyleSheet(rowsFontStyle, this.grid.id + '_rstyle');
		    }  
		    
	    }	
	}

});


Sbi.registerWidget('table', {
	name: 'Table'
	, icon: 'js/src/ext4/sbi/cockpit/widgets/table/img/table_64x64_ico.png'
	, runtimeClass: 'Sbi.cockpit.widgets.table.TableWidget'
	, designerClass: 'Sbi.cockpit.widgets.table.TableWidgetDesigner'
});