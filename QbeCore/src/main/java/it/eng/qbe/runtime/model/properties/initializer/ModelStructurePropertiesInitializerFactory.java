/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.runtime.model.properties.initializer;

import it.eng.qbe.hibernate.datasource.hibernate.IHibernateDataSource;
import it.eng.qbe.jpa.datasource.jpa.IJpaDataSource;
import it.eng.qbe.jpa.datasource.jpa.JPADataSource;
import it.eng.qbe.runtime.datasource.IDataSource;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class ModelStructurePropertiesInitializerFactory {

	public static IModelStructurePropertiesInitializer getDataMartStructurePropertiesInitializer(IDataSource dataSource) {
		IModelStructurePropertiesInitializer initializer;

		initializer = null;

		if (dataSource instanceof IHibernateDataSource) {
			initializer = new SimpleModelStructurePropertiesInitializer(dataSource);
		} else if (dataSource instanceof JPADataSource) {
			initializer = new SimpleModelStructurePropertiesInitializer((IJpaDataSource) dataSource);
		} else {
			throw new RuntimeException("Impossible to load datamart structure from a datasource of type [" + dataSource.getClass().getName() + "]");
		}

		return initializer;
	}
}
