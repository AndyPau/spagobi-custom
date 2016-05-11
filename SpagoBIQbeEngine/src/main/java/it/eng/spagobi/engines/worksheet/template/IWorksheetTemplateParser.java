/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.worksheet.template;

import java.util.Map;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public interface IWorksheetTemplateParser {
	WorksheetTemplate parse(Object template,  Map env);
	boolean hasInnerQbeQuery(Object template);
}
