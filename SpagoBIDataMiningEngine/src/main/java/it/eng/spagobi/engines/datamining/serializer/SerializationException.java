/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.datamining.serializer;

import it.eng.spagobi.utilities.exceptions.SpagoBIException;


/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class SerializationException extends SpagoBIException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6508446997091785836L;

	public SerializationException(String message) {
		super(message);
	}
	
	public SerializationException(String message, Throwable e) {
		super(message, e);
	}
}
