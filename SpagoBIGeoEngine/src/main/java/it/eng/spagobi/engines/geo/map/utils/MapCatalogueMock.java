/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.geo.map.utils;

import it.eng.spago.configuration.ConfigSingleton;

import java.io.File;
import java.net.MalformedURLException;

// TODO: Auto-generated Javadoc
/**
 * The Class MapCatalogueMock.
 * 
 * @author Andrea Gioia
 */
public class MapCatalogueMock {
	
	/**
	 * Gets the standard hierarchy.
	 * 
	 * @return the standard hierarchy
	 */
	public static String getStandardHierarchy() {	
		return "<HIERARCHY name=\"default\" table_name=\"gerarchia_geo\">"
			+ " <LEVEL name=\"unita_urbanistiche\" column_id=\"cod_uu\"  column_desc=\"desc_uu\" feature_name=\"unita_urbanistiche\"/>" 
			+ "	<LEVEL name=\"circoscrizioni\" column_id=\"cod_circoscrizione\"  column_desc=\"desc_circoscrizione\" feature_name=\"circoscrizioni\"/>"             
			+ "</HIERARCHY>";
	}
	
	/**
	 * Gets the map url.
	 * 
	 * @param mapName the map name
	 * 
	 * @return the map url
	 */
	public static String getMapUrl(String mapName) {
		if(mapName.equalsIgnoreCase("circoscrizioniBis")) mapName = "circoscrizioni";
		
		File file = new File(ConfigSingleton.getRootPath() + "/maps/genova/" + mapName + ".svg");
		try {
			return file.toURL().toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the map names by feature.
	 * 
	 * @param featureName the feature name
	 * 
	 * @return the map names by feature
	 */
	public static String[] getMapNamesByFeature(String featureName) {
		if(featureName.equalsIgnoreCase("circoscrizioni")) {
			return new String[]{"circoscrizioni", "circoscrizioniBis"};
		}
		return new String[]{featureName};
	}

	/**
	 * Gets the feature names in map.
	 * 
	 * @param mapName the map name
	 * 
	 * @return the feature names in map
	 */
	public static String[] getFeatureNamesInMap(String mapName) {
		if(mapName.equalsIgnoreCase("circoscrizioniBis")) {
			return new String[]{
					"circoscrizioni"			
			};			
		}
		return new String[]{
				mapName			
		};
	}
}
