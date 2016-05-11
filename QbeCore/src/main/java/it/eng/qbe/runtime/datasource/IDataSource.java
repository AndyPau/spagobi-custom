/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.runtime.datasource;

import it.eng.qbe.jpa.datasource.IPersistenceManager;
import it.eng.qbe.runtime.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.transaction.ITransaction;
import it.eng.qbe.runtime.model.accessmodality.IModelAccessModality;
import it.eng.qbe.runtime.model.properties.IModelProperties;
import it.eng.qbe.runtime.model.structure.IModelStructure;
import it.eng.qbe.runtime.query.Query;
import it.eng.qbe.runtime.statement.IStatement;

import java.util.Locale;

/**
 * @author Andrea Gioia
 */
public interface IDataSource {

	String getName();

	IDataSourceConfiguration getConfiguration();

	IModelStructure getModelStructure();

	IModelAccessModality getModelAccessModality();

	void setDataMartModelAccessModality(IModelAccessModality modelAccessModality);

	IModelProperties getModelI18NProperties(Locale locale);

	void open();

	boolean isOpen();

	void close();

	IStatement createStatement(Query query);

	ITransaction getTransaction();

	IPersistenceManager getPersistenceManager();
}
