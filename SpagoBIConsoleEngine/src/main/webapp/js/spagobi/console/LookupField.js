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
  *  [list]
  * 
  * Authors
  * 
  * - Andrea Gioia (andrea.gioia@eng.it)
  */

Ext.ns("Sbi.console");

Sbi.console.LookupField = function(config) {
	
	Ext.apply(this, config);
	
	this.store = config.store;
	if(config.cm){
	    this.cm = config.cm;
    }
	
	if(config.sm){
	    this.sm = config.sm;
    }
	
	if(config.valueField){
	    this.valueFieldName = config.valueField;
    }
	
	if(config.descField && config.descField !== ''){
	    this.displayFieldName = config.descField;
    }else{
    	 this.displayFieldName = config.valueField || '';
    }
	
	//displayField and valueField are yet setted only with the filtering management
	if(config.displayField && config.displayField !== ''){
    	 this.displayField = config.displayField || '';
    }
	
	if(config.valueField && config.valueField !== ''){
   	 this.valueField = config.valueField || '';
   }
	
	if(config.loadStore){
	    this.loadStore = config.loadStore;
    }
	
	this.store.on('metachange', function( store, meta ) {
		this.updateMeta( meta );
	}, this);
	this.store.on('load', function( store, records, options  ) {
		this.applySelection();		
	}, this);
		
	if(config.drawFilterToolbar!=null && config.drawFilterToolbar!=undefined && config.drawFilterToolbar==false){
		this.drawFilterToolbar = false;
	}else{
		this.drawFilterToolbar = true;
	}
	this.drawFilterToolbar = config.drawFilterToolbar;
	this.store.baseParams  = config.params;
	this.params = config.params;
	this.initWin();
	
	var c = Ext.apply({}, config, {
		triggerClass: 'x-form-search-trigger'
		, enableKeyEvents: true
		,  width: 150
	});   
	
	// constructor
	Sbi.console.LookupField.superclass.constructor.call(this, c);
	
	
	this.on("render", function(field) {
		field.trigger.on("click", function(e) {
			this.onLookUp(); 
		}, this);
	}, this);
	
	this.on("render", function(field) {
		field.el.on("keyup", function(e) {
			this.xdirty = true;
		}, this);
	}, this);
	

};

Ext.extend(Sbi.console.LookupField, Ext.form.TriggerField, {
    
	// ----------------------------------------------------------------------------------------
	// members
	// ----------------------------------------------------------------------------------------
    
	// STATE MEMBERS
	  valueField: null
	, valueFieldName: null
	, displayField: null
	, displayFieldName: null
	, columnField: null
    
    , drawFilterToolbar: null
    
    // oggetto (value: description, *)
    , xvalue: null
    // oggetto (value: description, *)
    , xselection: {} //null
    , xdirty: false
    , xTempValue: null
    , singleSelect: true
    
    , paging: true
    , start: 0 
    , limit: 20
    , loadStore: null
    
	// SUB-COMPONENTS MEMBERS
	, store: null
	, sm: null
	, cm: null
    , grid: null
    , win: null
    
       
   
    // ----------------------------------------------------------------------------------------
    // public methods
	// ----------------------------------------------------------------------------------------
    
    
    , getValue : function(){
		this.clean();
		var v = [];
		this.xvalue = this.xvalue || {};
	
		for(p in this.xvalue) {
			v.push(p);
		}
			
		if(this.singleSelect === true) {
			v = (v.length > 0)? v[0] : '';
		}
		return v;
	}

	/**
	 * v: 
	 *  - object -> multivalue with values/descriptions
	 *  - array -> multivalue with only values
	 *  - string -> single value
	 */
	, setValue : function(v){	 
		if(typeof v === 'object') {
			this.xvalue = {};
			
			if(v instanceof Array) {
				var t = {};
				for(var i = 0; i < v.length; i++) {
					t[ v[i] ] = v[i];
				}
				v = t;
			}
			Ext.apply(this.xvalue, v);
			var displayText = '';
			for(p in this.xvalue) {
				displayText += this.xvalue[p] + ';';
			}	
			if(this.singleSelect === true) {
				displayText = displayText.substr(0, displayText.length-1);
			}
			Sbi.console.LookupField.superclass.setValue.call(this, displayText);
		} else {
			this.xvalue = {};
			this.xvalue[v] = v;
			Sbi.console.LookupField.superclass.setValue.call(this, v);
		}
	}
	
	
    
    // private methods
    , initWin: function() {
    	if(!this.cm){
			this.cm = new Ext.grid.ColumnModel([
			   new Ext.grid.RowNumberer(),
		       {
		       	  header: "Data",
		          dataIndex: 'data',
		          width: 75
		       }
		    ]);
    	}
		
		var pagingBar = new Ext.PagingToolbar({
	        pageSize: this.limit,
	        store: this.store,
	        displayInfo: true,
	        displayMsg: '', //'Displaying topics {0} - {1} of {2}',
	        emptyMsg: "No topics to display",
	        
	        items:[
	               '->'
	               , {
	            	   text: LN('sbi.console.promptables.lookup.Annulla')
	            	   , listeners: {
		           			'click': {
		                  		fn: this.onCancel,
		                  		scope: this
		                	} 
	               		}
	               } , {
	            	   text: LN('sbi.console.promptables.lookup.Confirm')
	            	   , listeners: {
		           			'click': {
		                  		fn: this.onOk,
		                  		scope: this
		                	} 
	               		}
	               }
	        ]
	    });
		
		if(this.drawFilterToolbar){
			this.filteringToolbar = new Sbi.console.FilteringToolbar({store: this.store});
		}
		if (!this.sm){
			this.sm = new Ext.grid.CheckboxSelectionModel( {singleSelect: this.singleSelect } );
    	}
		this.sm.on('rowselect', this.onSelect, this);
		this.sm.on('rowdeselect', this.onDeselect, this);

		if(this.drawFilterToolbar){
			this.gridLookup = new Ext.grid.GridPanel({
				store: this.store
	   	     	, cm: this.cm
	   	     	, sm: this.sm
	   	     	, frame: false
	   	     	, border:false  
	   	     	, collapsible:false
	   	     	, loadMask: true
	   	     	, viewConfig: {
	   	        	forceFit:true
	   	        	, enableRowBody:true
	   	        	, showPreview:true
	   	     	}	
				, tbar: this.filteringToolbar
		        , bbar: pagingBar
			});
		}else{
			this.gridLookup = new Ext.grid.GridPanel({
				store: this.store
	   	     	, cm: this.cm
	   	     	, sm: this.sm
	   	     	, frame: false
	   	     	, border:false  
	   	     	, collapsible:false
	   	     	, loadMask: true
	   	     	, viewConfig: {
	   	        	forceFit:true
	   	        	, enableRowBody:true
	   	        	, showPreview:true
	   	     	}	
		        , bbar: pagingBar
			});
		}
		
		this.win = new Ext.Window({
			title: LN('sbi.console.promptables.lookup.Select') ,   
            layout      : 'fit',
            width       : 580,
            height      : 300,
            closeAction :'hide',
            plain       : true,
            items       : [this.gridLookup]
		});
	}
    
    , updateMeta: function(meta) {
    	if(this.gridLookup){			
			meta.fields[0] = new Ext.grid.RowNumberer();
			meta.fields[ meta.fields.length ] = this.sm;
			this.gridLookup.getColumnModel().setConfig(meta.fields);
			//sets the correct displayField
			for(i = 0; i < meta.fields.length; i++) {
				if (meta.fields[i].header == this.valueFieldName){
					this.valueField = meta.fields[i].name;
					break;
				}				
			}
			for(i = 0; i < meta.fields.length; i++) {
				if (meta.fields[i].header == this.displayFieldName){
					this.displayField = meta.fields[i].name;
					break;
				}				
			}
		} else {
		   alert('ERROR: store meta changed before grid instatiation');
		}
	}
    
    , resetSelection: function() {
    	this.xselection = Ext.apply({}, this.xvalue);    
   	}
    
    , onSelect: function(sm, rowIndex, record) {
    	if(this.singleSelect === true){
    		this.xselection = {};
    	}
    	this.xselection[ record.data[this.valueField] ] = record.data[this.displayField];
    }
    
    , onDeselect: function(sm, rowIndex, record) {
    	if( this.xselection[ record.data[this.displayField]] ) {
    		delete this.xselection[ record.data[this.displayField]];
    	}else if( this.xselection[ record.data[this.valueField]] ) {
    		delete this.xselection[ record.data[this.valueField]];
    	}   	
    }
    
    , applySelection: function() {
    	//this.resetSelection();
    	
    	if(this.gridLookup) {    		    		
			var selectedRecs = [];
			this.gridLookup.getStore().each(function(rec){
		        if(this.xselection[ rec.data[this.valueField]] !== undefined){
		        	selectedRecs.push(rec);
		        }
		    }, this);
			
			if(selectedRecs.length>0){
				this.sm.selectRecords(selectedRecs);
			}
		 }		
    }
	
    , clean: function() {
    	if(this.xdirty) {
    		
	    	var text = Sbi.console.LookupField.superclass.getValue.call(this);

	    	var values = text.split(';');

	    	this.xvalue = {};
	    	if(text.trim() === '') return;
	    	var ub = (this.singleSelect === true)? 1: values.length;
	    	for(var i = 0; i < ub; i++) {
	    		this.xvalue[ '' + values[i] ] = values[i];
	    	}
	    	this.xdirty = false;
    	}
    }
    
	, onLookUp: function() {
		this.resetSelection();
		this.clean();		
		this.win.show(this);
		
		var p = Ext.apply({}, {
			start: this.start
		  , limit: this.limit
		});
		this.store.load({params: p});
	}
	
	, onOk: function() {
		this.setValue(this.xselection);
		this.fireEvent('select', this.xselection);
		this.win.hide();			
	}
	
	, onCancel: function() {			
		this.resetSelection();
		this.win.hide();		
	}
});