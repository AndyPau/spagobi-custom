/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.jpa.statement.jpa;

import it.eng.qbe.runtime.query.Query;
import it.eng.qbe.runtime.statement.AbstractStatementOrderByClause;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JPQLStatementOrderByClause extends AbstractStatementOrderByClause {

	public static transient Logger logger = Logger.getLogger(JPQLStatementOrderByClause.class);

	public static String build(JPQLStatement parentStatement, Query query, Map<String, Map<String, String>> entityAliasesMaps) {
		JPQLStatementOrderByClause clause = new JPQLStatementOrderByClause(parentStatement);
		return clause.buildClause(query, entityAliasesMaps);
	}

	protected JPQLStatementOrderByClause(JPQLStatement statement) {
		parentStatement = statement;
	}

}
