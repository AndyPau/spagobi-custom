/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.spagobi.commons.deserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class DeserializerFactory {
	
	static Map<String, Deserializer> mappings;
	
	static {
		mappings = new HashMap();
		mappings.put( "application/json", new JSONDeserializer() );
		mappings.put( "application/xml", new XMLDeserializer() );
	}
	
	public static Deserializer getDeserializer(String mimeType) {
		return mappings.get( mimeType );
	}
}
