/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.jpa.datasource.transaction.jpa;

import it.eng.qbe.hibernate.datasource.transaction.hibernate.HibernateTransaction;
import it.eng.qbe.jpa.datasource.jpa.IJpaDataSource;
import it.eng.qbe.runtime.datasource.transaction.ITransaction;

import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;

/**
 * @authors Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class JPAHibernateTransaction implements ITransaction {

	private final IJpaDataSource dataSource;
	private Session session;

	public JPAHibernateTransaction(IJpaDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.qbe.datasource.transaction.ITransaction#open()
	 */
	@Override
	public void open() {
		session = ((HibernateEntityManager) dataSource.getEntityManager()).getSession();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.qbe.datasource.transaction.ITransaction#close()
	 */
	@Override
	public void close() {
		// we use the active session so we should not close it
		// session.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.qbe.datasource.transaction.ITransaction#getSQLConnection()
	 */
	@Override
	public java.sql.Connection getSQLConnection() {
		return getConnection(this.session);
	}

	public static Connection getConnection(Session session) {
		return HibernateTransaction.getConnection(session);
	}

}