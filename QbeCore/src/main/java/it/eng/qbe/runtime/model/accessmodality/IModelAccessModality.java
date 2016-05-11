/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.runtime.model.accessmodality;

import it.eng.qbe.runtime.datasource.IDataSource;
import it.eng.qbe.runtime.model.structure.IModelEntity;
import it.eng.qbe.runtime.model.structure.IModelField;
import it.eng.qbe.runtime.query.Filter;
import it.eng.qbe.runtime.query.Query;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IModelAccessModality {

	boolean isEntityAccessible(IModelEntity entity);

	/**
	 * Checks if is field accessible.
	 *
	 * @param tableName
	 *            the table name
	 * @param fieldName
	 *            the field name
	 *
	 * @return true, if is field accessible
	 */
	boolean isFieldAccessible(IModelField field);

	/**
	 * Gets the entity filter conditions.
	 *
	 * @param entityName
	 *            the entity name
	 *
	 * @return the entity filter conditions
	 */
	List<Filter> getEntityFilterConditions(String entityName);

	/**
	 * Gets the entity filter conditions.
	 *
	 * @param entityName
	 *            the entity name
	 * @param parameters
	 *            the parameters
	 *
	 * @return the entity filter conditions
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	List getEntityFilterConditions(String entityName, Properties parameters);

	public Boolean getRecursiveFiltering();

	public void setRecursiveFiltering(Boolean recursiveFiltering);

	public Query getFilteredStatement(Query query, IDataSource iDataSource, Map userProfileAttributes);
}
