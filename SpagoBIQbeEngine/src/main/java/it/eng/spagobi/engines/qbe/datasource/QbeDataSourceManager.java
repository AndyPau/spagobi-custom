/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.qbe.datasource;

import it.eng.qbe.dataset.datasource.dataset.DataSetDataSource;
import it.eng.qbe.dataset.datasource.dataset.DataSetDriver;
import it.eng.qbe.runtime.datasource.DriverManager;
import it.eng.qbe.runtime.datasource.IDataSource;
import it.eng.qbe.runtime.datasource.configuration.CompositeDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.DataSetDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.FileDataSourceConfiguration;
import it.eng.spagobi.services.proxy.MetamodelServiceProxy;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.utils.datamart.DefaultEngineDatamartRetriever;
import it.eng.spagobi.utilities.engines.EngineConstants;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia
 */
public class QbeDataSourceManager {

	private static QbeDataSourceManager instance;

	/**
	 * Gets the single instance of QbeDataSourceManager.
	 *
	 * @return single instance of QbeDataSourceManager
	 */
	public static QbeDataSourceManager getInstance() {
		if (instance == null) {
			instance = new QbeDataSourceManager();
		}

		return instance;
	}

	public IDataSource getDataSource(List<String> dataMartNames, Map<String, Object> dataSourceProperties, boolean useCache) {
		if (dataSourceProperties != null && dataSourceProperties.get(EngineConstants.ENV_DATASETS) != null) {
			return getDataSourceFromDataSet(dataSourceProperties, useCache);
		} else {
			return getORMDataSource(dataMartNames, dataSourceProperties, useCache);
		}
	}

	private IDataSource getORMDataSource(List<String> dataMartNames, Map<String, Object> dataSourceProperties, boolean useCache) {
		IDataSource dataSource;

		CompositeDataSourceConfiguration compositeConfiguration = new CompositeDataSourceConfiguration();
		Iterator<String> it = dataSourceProperties.keySet().iterator();
		while (it.hasNext()) {
			String propertyName = it.next();
			compositeConfiguration.loadDataSourceProperties().put(propertyName, dataSourceProperties.get(propertyName));
		}

		boolean isJPA = false;
		File modelJarFile;
		FileDataSourceConfiguration c;

		MetamodelServiceProxy metamodelProxy = (MetamodelServiceProxy) dataSourceProperties.get("metadataServiceProxy");
		DefaultEngineDatamartRetriever jarFileRetriever = new DefaultEngineDatamartRetriever(metamodelProxy);
		List<File> modelJarFiles = new ArrayList<File>();
		for (int i = 0; i < dataMartNames.size(); i++) {
			modelJarFile = jarFileRetriever.retrieveDatamartFile(dataMartNames.get(i));
			modelJarFiles.add(modelJarFile);
			c = new FileDataSourceConfiguration(dataMartNames.get(i), modelJarFile);
			compositeConfiguration.addSubConfiguration(c);
		}

		isJPA = jarFileRetriever.isAJPADatamartJarFile(modelJarFiles.get(0));
		if (modelJarFiles.size() > 1) {
			for (int i = 1; i < modelJarFiles.size(); i++) {
				modelJarFile = modelJarFiles.get(i);
				boolean b = jarFileRetriever.isAJPADatamartJarFile(modelJarFile);
				if (isJPA != b) {
					throw new SpagoBIRuntimeException("Impossible to create a composite datasource from different datasource type");
				}
			}
		}

		String driverName = isJPA ? "jpa" : "hibernate";
		dataSource = DriverManager.getDataSource(driverName, compositeConfiguration, useCache);

		return dataSource;
	}

	public IDataSource getDataSourceFromDataSet(Map<String, Object> dataSourceProperties, boolean useCache) {

		IDataSource dataSource;
		List<IDataSet> dataSets = (List<IDataSet>) dataSourceProperties.get(EngineConstants.ENV_DATASETS);
		dataSourceProperties.remove(EngineConstants.ENV_DATASETS);

		CompositeDataSourceConfiguration compositeConfiguration = new CompositeDataSourceConfiguration(DataSetDataSource.EMPTY_MODEL_NAME);
		Iterator<String> it = dataSourceProperties.keySet().iterator();
		while (it.hasNext()) {
			String propertyName = it.next();
			compositeConfiguration.loadDataSourceProperties().put(propertyName, dataSourceProperties.get(propertyName));
		}

		for (int i = 0; i < dataSets.size(); i++) {
			DataSetDataSourceConfiguration c = new DataSetDataSourceConfiguration((dataSets.get(i)).getLabel(), dataSets.get(i));
			compositeConfiguration.addSubConfiguration(c);
		}

		dataSource = DriverManager.getDataSource(DataSetDriver.DRIVER_ID, compositeConfiguration, useCache);

		return dataSource;
	}
}
