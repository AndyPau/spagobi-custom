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
  * Public Functions
  * 
  *  [list]
  * 
  * 
  * Authors
  * 
  * - Antonella Giachino (antonella.giachino@eng.it)
  */
/*
Ext.define('Sbi.locale', {
//	statics: {
		 locale.dummyFormatter = function(v){return v;};
		 Sbi.locale.formatters = {
			int: Sbi.locale.dummyFormatter,
			float: Sbi.locale.dummyFormatter,
			string: Sbi.locale.dummyFormatter,		
			date: Sbi.locale.dummyFormatter,		
			boolean: Sbi.locale.dummyFormatter,
			html: Sbi.locale.dummyFormatter
		};


		if(Sbi.chart.commons.Format){
			if(Sbi.locale.formats) {
				Sbi.locale.formatters.int  = Sbi.chart.commons.Format.numberRenderer(Sbi.locale.formats['int']);		
				Sbi.locale.formatters.float  = Sbi.chart.commons.Format.numberRenderer(Sbi.locale.formats['float']);		
				Sbi.locale.formatters.string  = Sbi.chart.commons.Format.stringRenderer(Sbi.locale.formats['string']);		
				Sbi.locale.formatters.date    = Sbi.chart.commons.Format.dateRenderer(Sbi.locale.formats['date']);		
				Sbi.locale.formatters.boolean = Sbi.chart.commons.Format.booleanRenderer(Sbi.locale.formats['boolean']);
				Sbi.locale.formatters.html    = Sbi.chart.commons.Format.htmlRenderer();
			} else {
				Sbi.locale.formatters.int  = Sbi.chart.commons.Format.numberRenderer( );	
				Sbi.locale.formatters.float  = Sbi.chart.commons.Format.numberRenderer( );	
				Sbi.locale.formatters.string  = Sbi.chart.commons.Format.stringRenderer( );		
				Sbi.locale.formatters.date    = Sbi.chart.commons.Format.dateRenderer( );		
				Sbi.locale.formatters.boolean = Sbi.chart.commons.Format.booleanRenderer( );
				Sbi.locale.formatters.html    = Sbi.chart.commons.Format.htmlRenderer();
			}
		};


		Sbi.locale.localize = function(key) {
			if(!Sbi.locale.ln) return key;
			return Sbi.locale.ln[key] || key;
		};	
		
		// alias
		LN = Sbi.locale.localize;
		FORMATTERS = Sbi.locale.formatters;

	//}//statics
});
*/

Ext.ns("Sbi.locale");

Sbi.locale.dummyFormatter = function(v){return v;};

Sbi.locale.formatters = {
	//number: Sbi.locale.dummyFormatter,
	'int': Sbi.locale.dummyFormatter,
	'float': Sbi.locale.dummyFormatter,
	'string': Sbi.locale.dummyFormatter,		
	'date': Sbi.locale.dummyFormatter,		
	'boolean': Sbi.locale.dummyFormatter,
	'html': Sbi.locale.dummyFormatter
};


if(Sbi.chart.commons.Format){
	if(Sbi.locale.formats) {
		Sbi.locale.formatters['int']  = Sbi.chart.commons.Format.numberRenderer(Sbi.locale.formats['int']);		
		Sbi.locale.formatters['float'] = Sbi.chart.commons.Format.numberRenderer(Sbi.locale.formats['float']);		
		Sbi.locale.formatters['string'] = Sbi.chart.commons.Format.stringRenderer(Sbi.locale.formats['string']);		
		Sbi.locale.formatters['date'] = Sbi.chart.commons.Format.dateRenderer(Sbi.locale.formats['date']);		
		Sbi.locale.formatters['boolean'] = Sbi.chart.commons.Format.booleanRenderer(Sbi.locale.formats['boolean']);
		Sbi.locale.formatters['html']   = Sbi.chart.commons.Format.htmlRenderer();
	} else {
		Sbi.locale.formatters['int']   = Sbi.chart.commons.Format.numberRenderer( );	
		Sbi.locale.formatters['float']  = Sbi.chart.commons.Format.numberRenderer( );	
		Sbi.locale.formatters['string'] = Sbi.chart.commons.Format.stringRenderer( );		
		Sbi.locale.formatters['date']   = Sbi.chart.commons.Format.dateRenderer( );		
		Sbi.locale.formatters['boolean']= Sbi.chart.commons.Format.booleanRenderer( );
		Sbi.locale.formatters['html']  = Sbi.chart.commons.Format.htmlRenderer();
	}
}


Sbi.locale.localize = function(key) {
	var value = messageResource.get(key, 'messages');
	
	//If the message is not defined in the current language the english message is used
	if (value == key){
		value = messageResource.get(key, 'messages', 'en_US');
	}
	return value || key;
};

//alias
LN = Sbi.locale.localize;
FORMATTERS = Sbi.locale.formatters;
