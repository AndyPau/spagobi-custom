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
 * [list]
 * 
 * 
 * Public Events
 * 
 * [list]
 * 
 * Authors - Alberto Ghedin
 */
Ext.ns("Sbi.widgets");

Sbi.widgets.TreeLookUpField = function(config) {

	var defaultSettings = Ext.apply({}, config, {
		triggerClass : 'tree-look-up',
		enableKeyEvents : true,
		width : 150,
		allowInternalNodeSelection: false,
		readOnly: true
	});

	this.rootConfig = {
		text : 'root',
		triggerClass : 'tree-look-up',
		expanded : true,
		id : 'lovroot___SEPA__0'
	};

	if(Sbi.settings && Sbi.settings.widgets && Sbi.settings.widgets.TreeLookUpField) {
		defaultSettings = Ext.apply(defaultSettings, Sbi.settings.widgets.TreeLookUpField);
	}

	
	defaultSettings = Ext.apply(defaultSettings, config);
	Ext.apply(this, defaultSettings);
	this.initWin();

	// constructor
	Sbi.widgets.TreeLookUpField.superclass.constructor.call(this, defaultSettings);

	this.on("render", function(field) {
		field.trigger.on("click", function(e) {
			if (!this.disabled) {
				this.onLookUp();
			}
		}, this);
	}, this);

	this.on("render", function(field) {
		field.el.on("keyup", function(e) {
			this.xdirty = true;
		}, this);
	}, this);

	this.addEvents('select');

};

Ext.extend(Sbi.widgets.TreeLookUpField, Ext.form.TriggerField, {

	win : null,
	xvalues : new Array()

	,
	initWin : function() {
		var thisPanel = this;
		this.xvaluesUnchecked = new Array();
		this.xdescriptionsUnchecked = new Array();
		this.treeLoader = new Ext.tree.TreeLoader({
			dataUrl : this.service,
			baseParams : this.params,
			createNode : function(attr) {
				attr.text = attr.description;

				if (attr.leaf) {
					attr.iconCls = 'parameter-leaf';
				}
				
				if ((attr.leaf || thisPanel.allowInternalNodeSelection) && thisPanel.multivalue) {
					if (thisPanel.xvalues && thisPanel.xvalues.indexOf(attr.value) >= 0) {
						attr.checked = true;
					} else {
						attr.checked = false;
					}
				}
				
				attr.uiProvider = Ext.extend(Ext.tree.TreeNodeUI, {
                    // private
                    onDblClick:function (e) {
                        e.preventDefault();
                        if (this.disabled) {
                            return;
                        }
                        if (this.fireEvent("beforedblclick", this.node, e) !== false) {
                             if (this.checkbox) {
                                 this.toggleCheck();
                             }
                             if (!this.animating && this.node.isExpandable()) {
                                 this.node.toggle();
                             }
                            this.fireEvent("dblclick", this.node, e);
                        }
                    }
                });
				
				var node = Ext.tree.TreeLoader.prototype.createNode.call(this,
						attr);
				
				
				if ((thisPanel.allowInternalNodeSelection || attr.leaf) && thisPanel.multivalue) {
					node.on('checkchange', function(node, checked){
						//if the check is checked, we remove it from the unchecked list
						if(checked){
							var index = thisPanel.xvaluesUnchecked.indexOf(node.attributes.value);
							if(index>=0){
								thisPanel.xvaluesUnchecked.splice(index,1);
								thisPanel.xdescriptionsUnchecked.splice(index,1);
							}
							
						}else{
							thisPanel.xvaluesUnchecked.push(node.attributes.value);
							thisPanel.xdescriptionsUnchecked.push(node.attributes.description);
						}
					}, this);
				}
				
				
				
				if (!thisPanel.multivalue && (thisPanel.allowInternalNodeSelection || attr.leaf) ) {
					node.on('click',
							function(node, e) {
								thisPanel.onOkSingleValue(node);
							}, this);
				}
				

				if (thisPanel.multivalue ){
					node.on('beforedblclick',
							function(node, e) {
						return false;
					}, this);
					node.on('beforeclick',
							function(node, e) {
						return false;
					}, this);
				}
				return node;
			}
		});

		this.tree = new Ext.tree.TreePanel({
			width : 200,
			autoScroll : true,
			rootVisible : false,
			loader : this.treeLoader,
			root : new Ext.tree.AsyncTreeNode(this.rootConfig)
		// , rootVisible: false
		});
		

		this.win = new Ext.Window({
			title : LN('sbi.lookup.Select'),
			layout : 'fit',
			width : 580,
			height : 300,
			closeAction : 'hide',
			plain : true,
			items : [ this.tree ],
			buttons : [ {
				text : LN('sbi.lookup.Annulla'),
				listeners : {
					'click' : {
						fn : this.onCancel,
						scope : this
					}
				}
			}, {
				text : LN('sbi.lookup.Confirm'),
				listeners : {
					'click' : {
						fn : this.onOk,
						scope : this
					}
				}
			} ]
		});

	}

	,
	onLookUp : function() {
		this.fireEvent('lookup', this);
		this.win.show(this);
		if (this.xreset) {
			this.reloadTree();
			this.xreset = false;
		}
	}

	,
	onOk : function() {
		var v = this.getCheckedValue();
		this.setValue(v);
		var d = this.getCheckedDescription();
		this.setRawValue(d);
		this.fireEvent('select', this, v);
		this.win.hide();
	}

	,
	onOkSingleValue : function(value) {
		if (!this.multivalue){
			this.setValue(value.attributes.value);
			this.setRawValue(value.attributes.description);
			this.fireEvent('select', this, value.attributes.value);
			this.win.hide();
		}
	}
	
	,
	onCancel : function() {
		this.win.hide();
	}

	,
	setValue : function(values) {
		Sbi.debug('[TreeLookUpField.setValue] : IN, values = [' + values + ']');
		var pvalues = "";

		if (values) {
			if(values instanceof Array) {
				for ( var i = 0; i < values.length; i++) {
					pvalues = pvalues + ";" + (values[i]);
				}
				pvalues = pvalues.substring(1);
			}else{
				pvalues = values;
				values = values.split(";");
			}
		} else {
			values = null;
			//this.reloadTree();
		}
		Sbi.debug('[TreeLookUpField.setValue] : values = [' + values + ']');
		this.xStartingValues = values;
		this.xvalues = values;
		Sbi.debug('[TreeLookUpField.setValue] : pvalues = [' + pvalues + ']');
		Sbi.widgets.TreeLookUpField.superclass.setValue.call(this, pvalues);
		
		this.fireEvent('select', this, values);
		
		Sbi.debug('[TreeLookUpField.setValue] : OUT');
	}

	,
	setRawValue : function(values) {
		Sbi.debug('[TreeLookUpField.setRawValue] : IN, values = [' + values + ']');
		var pvalues = "";
		if (values) {
			if(values instanceof Array) {
				for ( var i = 0; i < values.length; i++) {
					pvalues = pvalues + ";" + (values[i]);
				}
				pvalues = pvalues.substring(1);
			}else{
				pvalues = values;
				values = values.split(";");
			}
		}else{
			values=null;
		}
		Sbi.debug('[TreeLookUpField.setRawValue] : pvalues = [' + pvalues + ']');
		Sbi.widgets.TreeLookUpField.superclass.setRawValue.call( this, pvalues);
		this.xStartingDescriptions = values;
		this.xdescriptions = values;
		Sbi.debug('[TreeLookUpField.setRawValue] : OUT');
	}

	,
	getCheckedValue : function() {
		var checked = this.tree.getChecked();
		var values = [];
		var valuesToReturn = [];
		var value;
		if (checked) {
			for ( var i = 0; i < checked.length; i++) {
				values.push(checked[i].attributes.value);			
			}
			
			//merge the selected values with the initial values (the ones coming from the setValue)
			//necessary because the tree is asyncr
			values = this.mergeArrays(values, this.xStartingValues);
			for ( var i = 0; i < values.length; i++) {
				value = values[i];
				if(this.xvaluesUnchecked== null || this.xvaluesUnchecked== undefined || this.xvaluesUnchecked.indexOf(value)<0){
					valuesToReturn.push(value);			
				}
			}
		}
		this.xvalues = valuesToReturn;
		return valuesToReturn;
	}
	
	
	
	,
	getCheckedDescription : function() {
		var checked = this.tree.getChecked();
		var descriptions = [];
		var descriptionsToReturn = [];
		var description;
		if (checked) {
			for ( var i = 0; i < checked.length; i++) {
				descriptions.push(checked[i].attributes.description);			
			}
			
			//merge the selected values with the initial values (the ones coming from the setValue)
			//necessary because the tree is asyncr
			descriptions = this.mergeArrays(descriptions, this.xStartingDescriptions);
			for ( var i = 0; i < descriptions.length; i++) {
				description = descriptions[i];
				if(this.xdescriptionsUnchecked== null || this.xdescriptionsUnchecked== undefined || this.xdescriptionsUnchecked.indexOf(description)<0){
					descriptionsToReturn.push(description);			
				}
			}
		}
		this.xdescriptions = descriptionsToReturn;
		return descriptionsToReturn;
	}

	,
	getValue : function() {
		if(this.xvalues)
			return this.xvalues
		return "";
	}
	
	,
	getRawValue : function() {
		var toReturn = "";
		if (this.xdescriptions) {//creates the string of the descriptions
			for ( var i = 0; i < this.xdescriptions.length; i++) {
				toReturn = toReturn+";"+this.xdescriptions[i];
			}
			toReturn = toReturn.substring(1);//remove this first ;
		} else {
			toReturn = Sbi.widgets.TreeLookUpField.superclass.getRawValue.call(this);
		}
		return toReturn;
	}
	
	, reset : function(){
		Sbi.debug('[TreeLookUpField.reset] : IN');
		this.xvalues = "";
		this.xdescriptions = "";
		var delNode;
	    while (delNode = this.tree.root.childNodes[0]) {
	    	this.tree.root.removeChild(delNode);
	    }
		Sbi.widgets.TreeLookUpField.superclass.reset.call( this);
		this.xreset = true;
		Sbi.debug('[TreeLookUpField.reset] : OUT');
	}

	// if the parameters has been change we reload the tree
	,
	reloadTree : function() {
		this.getRootNode().reload();
	}
	
	,trim: function(string){
		if(string){
			while(string!=null && string.length>0 && string[0]==' '){
				string = string.substring(1);
			}
			while(string!=null && string.length>0 && string[string.length-1]==' '){
				string = string.substring(0,string.length-1);
			}
		}
		return string;
	}
	
	,mergeArrays: function(array1, array2){
		if(array1){
			if(array2){
				for(var i=0; i<array2.length; i++){
					if(array1.indexOf(array2[i])<0){
						array1.push(array2[i]);
					}
				}
			}
			return  array1
		}else{
			return  array2;
		}
	}
	
	,
	getRootNode: function () {
		return this.tree.getRootNode();
	}
	
});