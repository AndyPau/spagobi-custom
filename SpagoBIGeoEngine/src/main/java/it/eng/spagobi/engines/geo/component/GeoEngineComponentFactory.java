/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.geo.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.GeoEngineConstants;
import it.eng.spagobi.engines.geo.GeoEngineException;
import it.eng.spagobi.engines.geo.datamart.provider.IDataMartProvider;
import it.eng.spagobi.engines.geo.map.provider.IMapProvider;
import it.eng.spagobi.engines.geo.map.renderer.IMapRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class GeoEngineComponentFactory.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class GeoEngineComponentFactory {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(GeoEngineComponentFactory.class);
	
    
	/**
	 * Builds the.
	 * 
	 * @param geoEngineComponentClassName the geo engine component class name
	 * @param conf the conf
	 * @param env the env
	 * 
	 * @return the i geo engine component
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public static IGeoEngineComponent build(String geoEngineComponentClassName, Object conf, Map env) 
	throws GeoEngineException {
		
		IGeoEngineComponent geoEngineComponent = null;
		
		logger.debug("IN");
		
		try {
			geoEngineComponent = (IGeoEngineComponent) Class.forName(geoEngineComponentClassName).newInstance();
		} catch (InstantiationException e) {
			logger.error("Impossible to instatiate component of type: " + geoEngineComponentClassName);
			throw new GeoEngineException("Impossible to instatiate component of type: " + geoEngineComponentClassName, e);
		} catch (IllegalAccessException e) {
			logger.error("Impossible to instatiate component of type: " + geoEngineComponentClassName);
			throw new GeoEngineException("Impossible to instatiate component of type: " + geoEngineComponentClassName, e);
		} catch (ClassNotFoundException e) {
			GeoEngineException geoException;
			logger.error("Impossible to instatiate component of type: " + geoEngineComponentClassName);
			String description = "Impossible to instatiate component of type: " + geoEngineComponentClassName;
			List hints = new ArrayList();
			hints.add("Check if the class name is wrong or mispelled");
			hints.add("Check if the class is on the class path");
			geoException =  new GeoEngineException("Impossible to instatiate component", e);
			geoException.setDescription(description);
			geoException.setHints(hints);
			throw geoException;
		}
		
		logger.debug("Component " + geoEngineComponentClassName + " created succesfully");
		geoEngineComponent.setEnv(env);
		geoEngineComponent.init(conf);
		logger.debug("Component " + geoEngineComponentClassName + " configurated succesfully");
		
		logger.debug("OUT");
		
        return geoEngineComponent;
	}
	
	/**
	 * Builds the map provider.
	 * 
	 * @param template the template
	 * @param env the env
	 * 
	 * @return the i map provider
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public static IMapProvider buildMapProvider(SourceBean template, Map env) throws GeoEngineException {
		IMapProvider mapProvider = null;
		SourceBean confSB = null;
		String className = null;
		
		logger.debug("IN");
		confSB = (SourceBean)template.getAttribute(GeoEngineConstants.MAP_PROVIDER_TAG);
		if(confSB == null) {
			logger.warn("Cannot find MapProvider configuration settings: tag name " + GeoEngineConstants.MAP_PROVIDER_TAG);
			logger.info("MapProvider configuration settings must be injected at execution time");
			return null;
		}
		className = (String)confSB.getAttribute(GeoEngineConstants.CLASS_NAME_ATTRIBUTE);
		if(className == null) {
			className = GeoEngineConstants.DEFAULT_MAP_PROVIDER;
			logger.warn("Cannot find MapProvider class attribute: " + GeoEngineConstants.CLASS_NAME_ATTRIBUTE);
			logger.warn("The default MapProvider implementation will be used: [" + className + "]");
		}
		logger.debug("Map provider class: " + className);
		logger.debug("Map provider configuration: " + confSB);
		
		mapProvider = (IMapProvider)build(className, confSB, env);
		logger.debug("IN");		
		
		return mapProvider;
	}
	
	/**
	 * Builds the map renderer.
	 * 
	 * @param template the template
	 * @param env the env
	 * 
	 * @return the i map renderer
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public static IMapRenderer buildMapRenderer(SourceBean template, Map env) throws GeoEngineException {
		IMapRenderer mapRenderer = null;
		SourceBean confSB = null;
		String className = null;
		
		logger.debug("IN");		
		confSB = (SourceBean)template.getAttribute(GeoEngineConstants.MAP_RENDERER_TAG);
		if(confSB == null) {
			logger.warn("Cannot find MapRenderer configuration settings: tag name " + GeoEngineConstants.MAP_RENDERER_TAG);
			logger.info("MapRenderer configuration settings must be injected at execution time");
			return null;
		}
		className = (String)confSB.getAttribute(GeoEngineConstants.CLASS_NAME_ATTRIBUTE);
		if(className == null) {
			className = GeoEngineConstants.DEFAULT_MAP_RENDERER;
			logger.warn("Cannot find MapRenderer class attribute: " + GeoEngineConstants.CLASS_NAME_ATTRIBUTE);
			logger.warn("The default MapRenderer implementation will be used: [" + className + "]");
		}
		logger.debug("Map renderer class: " + className);
		logger.debug("Map renderer configuration: " + confSB);		
		
		mapRenderer = (IMapRenderer)build(className, confSB, env);		
		logger.debug("OUT");
		
		return mapRenderer;
	}
	
	/**
	 * Builds the dataset provider.
	 * 
	 * @param template the template
	 * @param env the env
	 * 
	 * @return the i dataset provider
	 * 
	 * @throws GeoEngineException the geo engine exception
	 */
	public static IDataMartProvider buildDataMartProvider(SourceBean template, Map env) throws GeoEngineException {
		IDataMartProvider dataMartProvider = null;
		SourceBean confSB = null;
		String className = null;
		
		logger.debug("IN");
		confSB = (SourceBean)template.getAttribute(GeoEngineConstants.DATAMART_PROVIDER_TAG);
		if(confSB == null) {
			logger.warn("Cannot find DatasetProvider configuration settings: tag name " + GeoEngineConstants.DATAMART_PROVIDER_TAG);
			logger.info("DatasetProvider configuration settings must be injected at execution time");
			return null;
		}
		className = (String)confSB.getAttribute(GeoEngineConstants.CLASS_NAME_ATTRIBUTE);
		if(className == null) {
			className = GeoEngineConstants.DEFAULT_DATAMART_PROVIDER;
			logger.warn("Cannot find DatasetProvider class attribute: " + GeoEngineConstants.CLASS_NAME_ATTRIBUTE);
			logger.warn("The default DataMartProvider implementation will be used: [" + className + "]");
		} 
		
		logger.debug("Dataset provider class: " + className);
		logger.debug("Dataset provider configuration: " + confSB);
		
		dataMartProvider = (IDataMartProvider)build(className, confSB, env);;
		logger.debug("OUT");
		
		return dataMartProvider;
	}
}
