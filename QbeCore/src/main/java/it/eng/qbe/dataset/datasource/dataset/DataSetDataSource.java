/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.dataset.datasource.dataset;

import it.eng.qbe.dataset.datasource.transaction.dataset.DataSetTransaction;
import it.eng.qbe.dataset.model.structure.builder.dataset.DataSetModelStructureBuilder;
import it.eng.qbe.hive.statement.hive.HiveQLStatement;
import it.eng.qbe.jpa.datasource.IPersistenceManager;
import it.eng.qbe.jpa.datasource.jpa.JPADataSource;
import it.eng.qbe.runtime.datasource.AbstractDataSource;
import it.eng.qbe.runtime.datasource.configuration.CompositeDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.DataSetDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.transaction.ITransaction;
import it.eng.qbe.runtime.model.accessmodality.AbstractModelAccessModality;
import it.eng.qbe.runtime.model.structure.IModelStructure;
import it.eng.qbe.runtime.model.structure.builder.IModelStructureBuilder;
import it.eng.qbe.sql.datasource.sql.ISQLDataSource;
import it.eng.qbe.sql.statement.sql.SQLStatement;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DataSetDataSource extends AbstractDataSource implements ISQLDataSource {

	private final List<IDataSet> datasets;
	public static final String EMPTY_MODEL_NAME = "";
	public static final String DATASETS = "DATASETS";
	public Class statementType = SQLStatement.class;

	private static transient Logger logger = Logger.getLogger(JPADataSource.class);

	protected DataSetDataSource(String dataSourceName, IDataSourceConfiguration configuration) {
		logger.debug("Creating a new DataSetDataSource");
		setName(dataSourceName);
		dataMartModelAccessModality = new AbstractModelAccessModality();
		this.configuration = configuration;
		datasets = new ArrayList<IDataSet>();

		Assert.assertNotNull(configuration.loadDataSourceProperties(), "The properties of the datasource can not be empty");

		// // validate and set configuration
		if (configuration instanceof DataSetDataSourceConfiguration) {
			datasets.add(((DataSetDataSourceConfiguration) configuration).getDataset());
		} else if (configuration instanceof CompositeDataSourceConfiguration) {
			List<IDataSourceConfiguration> subConfigurations = ((CompositeDataSourceConfiguration) configuration).getSubConfigurations();
			for (int i = 0; i < subConfigurations.size(); i++) {
				IDataSourceConfiguration subConf = ((CompositeDataSourceConfiguration) configuration).getSubConfigurations().get(i);
				if (subConf instanceof DataSetDataSourceConfiguration) {
					datasets.add(((DataSetDataSourceConfiguration) subConf).getDataset());
				} else {
					Assert.assertUnreachable("Not suitable configuration to create a JPADataSource");
				}
			}

		} else {
			Assert.assertUnreachable("Not suitable configuration to create a JPADataSource");
		}
		logger.debug("Created a new JPADataSource");
		initStatementType();
	}

	public DataSetDataSourceConfiguration getDataSetDataSourceConfiguration() {
		return (DataSetDataSourceConfiguration) configuration;
	}

	public List<IDataSet> getDatasets() {
		return datasets;
	}

	public void open() {

	}

	public boolean isOpen() {
		return true;
	}

	public void close() {

	}

	@Override
	public IDataSource getToolsDataSource() {
		return getDataSourceForReading();
	}

	public IModelStructure getModelStructure() {
		IModelStructureBuilder structureBuilder;
		if (dataMartModelStructure == null) {
			structureBuilder = new DataSetModelStructureBuilder(this);
			dataMartModelStructure = structureBuilder.build();
		}

		return dataMartModelStructure;
	}

	public ITransaction getTransaction() {
		return new DataSetTransaction(this);
	}

	// TO-DO
	public IPersistenceManager getPersistenceManager() {
		return null;
	}

	public List<IDataSet> getRootEntities() {
		return datasets;
	}

	public Class getStatementType() {
		return statementType;
	}

	private void initStatementType() {
		IDataSource datasourceForReading = this.getDataSourceForReading();
		if (datasourceForReading != null) {
			if (datasourceForReading.getHibDialectName().toLowerCase().contains("hive")) {
				statementType = HiveQLStatement.class;
			}
		}

	}

	public IDataSource getDataSourceForReading() {
		return datasets.get(0).getDataSourceForReading();
	}
}
