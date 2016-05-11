/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.commons.serializer;

import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;

import java.util.Locale;

import org.json.JSONObject;

/**
 * @author Lazar Kostic (lazar.kostic@mht.net)
 */
public class ParametersJSONSerialize implements Serializer {

	public static final String ID = "ID";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String LENGTH = "LENGTH";
	public static final String LABEL = "LABEL";
	public static final String NAME = "NAME";
	public static final String MASK = "MASK";
	public static final String MODALITY = "MODALITY";
	public static final String FUNCTIONALFLAG = "FUNCTIONALFLAG";
	public static final String TEMPORALFLAG = "TEMPORALFLAG";
	public static final String INPUTTYPECD = "INPUTTYPECD";

	public Object serialize(Object o, Locale locale) throws SerializationException {

		JSONObject result = null;

		if (!(o instanceof Parameter)) {

			throw new SerializationException("ParameterJSONSerializer is unable to serialize object of type: " + o.getClass().getName());

		}

		try {

			Parameter parameter = null;
			result = new JSONObject();

			parameter = (Parameter) o;

			result.put(ID, parameter.getId());
			result.put(DESCRIPTION, parameter.getDescription());
			result.put(LENGTH, parameter.getLength());
			result.put(LABEL, parameter.getLabel());
			result.put(NAME, parameter.getName());
			result.put(FUNCTIONALFLAG, parameter.isFunctional());
			result.put(TEMPORALFLAG, parameter.isTemporal());
			result.put(MASK, parameter.getMask());
			result.put(MODALITY, parameter.getType() + "," + parameter.getTypeId());
			result.put(INPUTTYPECD, parameter.getType());

		} catch (Throwable t) {

			throw new SerializationException("An error occurred while serializing object: " + o, t);

		} finally {

		}

		return result;
	}
}
