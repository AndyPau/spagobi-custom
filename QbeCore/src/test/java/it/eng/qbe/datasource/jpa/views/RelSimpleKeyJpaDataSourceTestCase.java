/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.datasource.jpa.views;

import it.eng.qbe.jpa.datasource.jpa.JPADriver;
import it.eng.qbe.runtime.datasource.DriverManager;
import it.eng.qbe.runtime.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.IDataSourceConfiguration;

import java.io.File;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class RelSimpleKeyJpaDataSourceTestCase extends AbstractViewJpaDataSourceTestCase {

	private static final String QBE_FILE = "test-resources/jpa/views/relSimpleKey/dist/datamart.jar";

	@Override
	protected void setUpDataSource() {
		IDataSourceConfiguration configuration;

		modelName = "My Model";

		File file = new File(QBE_FILE);
		configuration = new FileDataSourceConfiguration(modelName, file);
		configuration.loadDataSourceProperties().put("connection", connection);
		dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration, false);
	}

	public void testQbeWithView() {
		doTests();
	}

	@Override
	public void doTests() {
		super.doTests();
		// add custom tests here
	}
}
