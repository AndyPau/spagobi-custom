/** SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
 
  
 
  
 
Ext.define('browserItems',{
	extend:'Ext.data.Model',
	//foolder atributes
	config: {
	fields: [{name: 'devRoles',		type:'array'},	         
	       	 {name: 'biObjects',	type:'array'},
	       	 {name: 'code',	type:'string'},
	       	 {name: 'codType',	type:'string'},	       	 
	       	 {name: 'id',		type:'integer'},
	       	 {name: 'testRoles',	type:'array'},
	       	 {name: 'parentId',		type:'integer'},
	       	 {name: 'prog',	type:'integer'},
	       	 {name: 'description',		type:'string'},
	       	 {name: 'name',		type:'string'},
	       	 {name: 'path',		type:'string'},
	       	 {name: 'execRoles',		type:'array'},	         
	       	 {name: 'actions',	type:'array'}
	       	 //document attributes
	       	 
	       	,{name: 'profiledVisibility',	type:'string'},
	       	{name: 'creationUser',	type:'string'},
	       	{name: 'visible',	type:'integer'},
	       	{name: 'exporters',	type:'array'},
	       	{name: 'engine',	type:'string'},
	       	{name: 'stateCode',	type:'string'},
	       	{name: 'typeCode',	type:'string'},
	       	{name: 'label',	type:'string'},
	       	{name: 'creationDate',	type:'string'},
	       	{name: 'engineid',	type:'integer'},
	       	{name: 'stateId',	type:'integer'},
	       	{name: 'description',	type:'string'},
	       	{name: 'encrypt',	type:'integer'},
	       	{name: 'functionalities',	type:'array'},
	       	{name: 'uuid',	type:'string'},
	       	{name: 'relname',	type:'string'},
	       	{name: 'typeId',	type:'integer'},
	       	{name: 'refreshSeconds',	type:'integer'},
	       	{name: 'leaf',	type:'boolean'}]
	}
});












