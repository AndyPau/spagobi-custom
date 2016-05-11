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

Ext.ns("Sbi");

Sbi.Sync = function(){
    
	// private variables
	var FORM_ID = 'download-form';
	var METHOD = 'post';
	var createHtmlFn;
	
    // public space
	return {
		
		form: null
		
		, request: function(o) {
			var f = this.getForm();
			if(o.method) f.method = o.method;
			
			f.action = o.url;
			
			if(f.method === 'post') {
				this.resetForm();
				if(o.params) {
					this.replaceDomHelper();
					for(p in o.params) {
						this.addHiddenInput(p, o.params[p]);
					}
					this.restoreDomHelper();
				}
			}else {
				if(o.params) {
					f.action = Ext.urlAppend(f.action, Ext.urlEncode(o.params) );
				}
			}
			
			f.submit();
			
		}
	
		, resetForm: function() {
			var f = Ext.get(FORM_ID);
			var childs = f.query('input');
			
			for(var i = 0, l = childs.length; i < l; i++) {
				 var child = Ext.get(childs[i]);			
				 child.remove();
				
			}
		}
		
		, replaceDomHelper: function() {
			createHtmlFn = Ext.DomHelper.createHtml;
			Ext.DomHelper.createHtml = function(o){
		        var b = '',
	            attr,
	            val,
	            key,
	            keyVal,
	            cn;

		        if(Ext.isString(o)){
		            b = o;
		        } else if (Ext.isArray(o)) {
		            for (var i=0; i < o.length; i++) {
		                if(o[i]) {
		                    b += createHtml(o[i]);
		                }
		            };
		        } else {
		            b += '<' + (o.tag = o.tag || 'div');
		            Ext.iterate(o, function(attr, val){
		                if(!/tag|children|cn|html$/i.test(attr)){
		                    if (Ext.isObject(val)) {
		                        b += ' ' + attr + '=\'';
		                        Ext.iterate(val, function(key, keyVal){
		                            b += key + ':' + keyVal + ';';
		                        });
		                        b += '\'';
		                    }else{
		                        b += ' ' + ({cls : 'class', htmlFor : 'for'}[attr] || attr) + '=\'' + val + '\'';
		                    }
		                }
		            });
		            // Now either just close the tag or try to add children and close the tag.
		            var emptyTags = /^(?:br|frame|hr|img|input|link|meta|range|spacer|wbr|area|param|col)$/i;
		            if (emptyTags.test(o.tag)) {
		                b += '/>';
		            } else {
		                b += '>';
		                if ((cn = o.children || o.cn)) {
		                    b += createHtml(cn);
		                } else if(o.html){
		                    b += o.html;
		                }
		                b += '</' + o.tag + '>';
		            }
		        }
		        return b;
			}
		}
		
		, restoreDomHelper: function() {
			if(!createHtmlFn) {
				alert("Impossible to restore createHtml in DomHelper object");
				return;
			}
			Ext.DomHelper.createHtml = createHtmlFn;
		}
		
		
		, addHiddenInput: function(name, value) {			
			var f = Ext.get(FORM_ID);
			var dh = Ext.DomHelper;
			dh.append(f, {
			    tag: 'input'
			    , type: 'hidden'
			    , name: name
			    , value: value
			});
		}
	
		, getForm: function() {
			//by unique request
			if(this.form === null) {
				this.form = document.getElementById(FORM_ID);
				if(!this.form) {
					var dh = Ext.DomHelper;
					this.form = dh.append(Ext.getBody(), {
					    id: FORM_ID
					    , tag: 'form'
					    , method: METHOD
					    , cls: 'download-form'
					});
				}
			}
			return this.form;
		}
	
	}
}();	