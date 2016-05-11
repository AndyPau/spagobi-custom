/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

/**
 * @authorAlberto Ghedin (alberto.ghedin@eng.it)
 */
package it.eng.spagobi.engines.whatif.cube;

import java.util.HashSet;
import java.util.Set;

import org.olap4j.metadata.Datatype;
import org.olap4j.metadata.Property;

public class LeafProperty implements Property {

	public boolean isVisible() {
		return true;
	}

	public String getUniqueName() {
		return "leafsNumber";
	}

	public String getName() {
		return "leafsNumber";
	}

	public String getDescription() {
		return "leafsNumber";
	}

	public String getCaption() {
		return "leafsNumber";
	}

	public Set<TypeFlag> getType() {
		return new HashSet<Property.TypeFlag>();
	}

	public Datatype getDatatype() {
		return Datatype.STRING;
	}

	public ContentType getContentType() {
		return ContentType.ID;
	}
};