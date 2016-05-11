/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.hibernate.datasource.hibernate;

import it.eng.qbe.hibernate.datasource.transaction.hibernate.HibernateTransaction;
import it.eng.qbe.hibernate.model.structure.builder.hibernate.HibernateModelStructureBuilder;
import it.eng.qbe.jpa.datasource.IPersistenceManager;
import it.eng.qbe.runtime.datasource.AbstractDataSource;
import it.eng.qbe.runtime.datasource.configuration.CompositeDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.transaction.ITransaction;
import it.eng.qbe.runtime.model.accessmodality.AbstractModelAccessModality;
import it.eng.qbe.runtime.model.structure.IModelStructure;
import it.eng.qbe.runtime.model.structure.builder.IModelStructureBuilder;
import it.eng.spagobi.tools.datasource.bo.IDataSource;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;

public class HibernateDataSource extends AbstractDataSource implements IHibernateDataSource {

	protected boolean classLoaderExtended = false;

	protected Configuration compositeHibernateConfiguration;
	protected SessionFactory compositeHibernateSessionFactory;

	protected Map<String, Configuration> configurationMap = new HashMap<String, Configuration>();
	protected Map<String, SessionFactory> sessionFactoryMap = new HashMap<String, SessionFactory>();

	private static transient Logger logger = Logger.getLogger(HibernateDataSource.class);

	protected HibernateDataSource(String dataSourceName, IDataSourceConfiguration configuration) {
		setName(dataSourceName);
		dataMartModelAccessModality = new AbstractModelAccessModality();

		// validate & set configuration
		if (configuration instanceof FileDataSourceConfiguration) {
			FileDataSourceConfiguration subConf = (FileDataSourceConfiguration) configuration;
			CompositeDataSourceConfiguration c = new CompositeDataSourceConfiguration(subConf.getModelName());
			c.addSubConfiguration(subConf);
			Iterator<String> it = subConf.loadDataSourceProperties().keySet().iterator();
			while (it.hasNext()) {
				String propertyName = it.next();
				c.loadDataSourceProperties().put(propertyName, subConf.loadDataSourceProperties().get(propertyName));
			}
			this.configuration = c;
		} else if (configuration instanceof CompositeDataSourceConfiguration) {
			CompositeDataSourceConfiguration c = (CompositeDataSourceConfiguration) configuration;
			if (c.getSubConfigurations() == null || c.getSubConfigurations().size() < 1) {
				throw new SpagoBIRuntimeException("Impossible to create HibernateDataSource. Datasource sub-configurations not defined");
			}
			for (int i = 0; i < c.getSubConfigurations().size(); i++) {
				if (!(c.getSubConfigurations().get(i) instanceof FileDataSourceConfiguration)) {
					throw new SpagoBIRuntimeException("Impossible to create HibernateDataSource. Unable to manage sub-configuration of type ["
							+ c.getSubConfigurations().get(i).getClass().getName() + "]");
				}
			}
			this.configuration = configuration;
		} else {
			throw new SpagoBIRuntimeException("Impossible to create HibernateDataSource. Unable to manage configuration of type ["
					+ configuration.getClass().getName() + "]");
		}

	}

	@Override
	public List<IDataSourceConfiguration> getSubConfigurations() {
		return ((CompositeDataSourceConfiguration) configuration).getSubConfigurations();
	}

	public boolean isCompositeDataSource() {
		boolean isComposite = false;
		if (configuration instanceof CompositeDataSourceConfiguration) {
			isComposite = (((CompositeDataSourceConfiguration) configuration).getSubConfigurations().size() > 1);
		}

		return isComposite;
	}

	@Override
	public synchronized void open() {
		logger.debug("IN");

		try {
			if (!isOpen()) {
				compositeHibernateConfiguration = buildEmptyConfiguration();

				addDatamarts();

				if (isCompositeDataSource()) {
					addDbLinks();
					compositeHibernateSessionFactory = compositeHibernateConfiguration.buildSessionFactory();
				} else {
					compositeHibernateSessionFactory = sessionFactoryMap.get(getSubConfigurations().get(0).getModelName());
				}

				classLoaderExtended = true;
			}
		} catch (Throwable t) {
			throw new SpagoBIRuntimeException("Impossible to open connection", t);
		} finally {
			logger.debug("OUT");
		}
	}

	@Override
	public boolean isOpen() {
		return compositeHibernateSessionFactory != null && compositeHibernateConfiguration != null;
	}

	@Override
	public void close() {
		compositeHibernateSessionFactory = null;
		compositeHibernateConfiguration = null;
		configurationMap = new HashMap<String, Configuration>();
		sessionFactoryMap = new HashMap<String, SessionFactory>();
		// classLoaderExtended = false;
	}

	protected void addDatamarts() {

		for (int i = 0; i < getSubConfigurations().size(); i++) {
			addDatamart((FileDataSourceConfiguration) getSubConfigurations().get(i), !classLoaderExtended);
		}
		classLoaderExtended = true;
	}

	protected void addDatamart(FileDataSourceConfiguration configuration, boolean extendClassLoader) {
		Configuration cfg = null;
		SessionFactory sf = null;

		if (configuration.getFile() == null)
			return;

		cfg = buildEmptyConfiguration();
		configurationMap.put(configuration.getModelName(), cfg);

		if (extendClassLoader) {
			updateCurrentClassLoader(configuration.getFile());
		}

		cfg.addJar(configuration.getFile());

		try {
			compositeHibernateConfiguration.addJar(configuration.getFile());
		} catch (Throwable t) {
			throw new RuntimeException("Cannot add datamart", t);
		}

		sf = cfg.buildSessionFactory();
		sessionFactoryMap.put(configuration.getModelName(), sf);
	}

	@Override
	public IDataSource getToolsDataSource() {
		IDataSource connection = (IDataSource) configuration.loadDataSourceProperties().get("datasource");
		return connection;
	}

	private Map getDbLinkMap() {
		Map dbLinkMap = (Map) configuration.loadDataSourceProperties().get("dblinkMap");
		return dbLinkMap;
	}

	protected Configuration buildEmptyConfiguration() {
		Configuration cfg = null;

		cfg = new Configuration();

		IDataSource connection = getToolsDataSource();

		if (connection.checkIsJndi()) {
			cfg.setProperty("hibernate.connection.datasource", connection.getJndi());
			cfg.setProperty("hibernate.validator.apply_to_ddl", "false");
			cfg.setProperty("hibernate.validator.autoregister_listeners", "false");
		} else {
			cfg.setProperty("hibernate.connection.url", connection.getUrlConnection());
			cfg.setProperty("hibernate.connection.password", connection.getPwd());
			cfg.setProperty("hibernate.connection.username", connection.getUser());
			cfg.setProperty("hibernate.connection.driver_class", connection.getDriver());
			cfg.setProperty("hibernate.validator.apply_to_ddl", "false");
			cfg.setProperty("hibernate.validator.autoregister_listeners", "false");
		}

		cfg.setProperty("hibernate.dialect", connection.getHibDialectClass());

		// Ingres does not support scrollable result set
		if ("org.hibernate.dialect.IngresDialect".equals(connection.getHibDialectClass())) {
			cfg.setProperty("hibernate.jdbc.use_scrollable_resultset", "false");
		}

		cfg.setProperty("hibernate.cglib.use_reflection_optimizer", "true");
		cfg.setProperty("hibernate.show_sql", "false");

		return cfg;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getConfiguration()
	 */
	@Override
	public Configuration getHibernateConfiguration() {
		if (isOpen() == false) {
			open();
		}
		return compositeHibernateConfiguration;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getSessionFactory()
	 */
	@Override
	public SessionFactory getHibernateSessionFactory() {
		if (isOpen() == false) {
			open();
		}
		return compositeHibernateSessionFactory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.qbe.datasource.IHibernateDataSource#getSessionFactory(java.lang.String)
	 */
	@Override
	public SessionFactory getHibernateSessionFactory(String dmName) {
		if (compositeHibernateSessionFactory == null)
			open();
		return sessionFactoryMap.get(dmName);
	}

	public Configuration getConfiguration(String dmName) {
		if (compositeHibernateConfiguration == null)
			open();
		return configurationMap.get(dmName);
	}

	protected void addDbLink(String modelName, Configuration srcCfg, Configuration dstCfg) {

		String dbLink = null;
		PersistentClass srcPersistentClass = null;
		PersistentClass dstPersistentClass = null;
		String targetEntityName = null;
		Table targetTable = null;

		dbLink = (String) getDbLinkMap().get(modelName);
		if (dbLink != null) {
			Iterator it = srcCfg.getClassMappings();
			while (it.hasNext()) {
				srcPersistentClass = (PersistentClass) it.next();
				targetEntityName = srcPersistentClass.getEntityName();
				dstPersistentClass = dstCfg.getClassMapping(targetEntityName);
				targetTable = dstPersistentClass.getTable();
				targetTable.setName(targetTable.getName() + "@" + dbLink);
			}
		}

	}

	protected void addDbLinks() {
		Configuration cfg = null;

		for (int i = 0; i < getSubConfigurations().size(); i++) {
			String modelName = getSubConfigurations().get(i).getModelName();
			cfg = configurationMap.get(modelName);
			addDbLink(modelName, cfg, compositeHibernateConfiguration);
		}
	}

	@Override
	public IModelStructure getModelStructure() {
		IModelStructureBuilder structureBuilder;
		if (dataMartModelStructure == null) {
			structureBuilder = new HibernateModelStructureBuilder(this);
			dataMartModelStructure = structureBuilder.build();
		}

		return dataMartModelStructure;
	}

	@Override
	public ITransaction getTransaction() {
		return new HibernateTransaction(this);
	}

	@Override
	public IPersistenceManager getPersistenceManager() {
		// TODO Auto-generated method stub
		return new HibernatePersistenceManager(this);
	}

}
