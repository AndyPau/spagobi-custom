/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engines.accessibility.dao;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.utilities.StringUtilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class QueryExecutor {
	private static transient Logger logger = Logger.getLogger(QueryExecutor.class);
	
	/**Executes query on passed connection, replacing execution parameters.
	 * @param con database connection
	 * @param query a string representing the query to execute
	 * @param parameters parameters ti replace
	 * @return resultset in xml format
	 * @throws Exception
	 */
	public static String executeQuery(Connection con, String query, HashMap<String, String> parameters) throws Exception{
		String xml = null;
		if(con == null || query == null){
			logger.error("Unable to execute query. Missing query or connection.");
			throw new Exception("Unable to execute query. Missing query or connection.");
		}
		if(!canExecute(query)){
			logger.error("This is not a query! ");
			throw new Exception("Cannot to execute query. This is not a query!");
		}
		try {  
			
	        Statement statement = con.createStatement();
	        HashMap<String, String> parType = new HashMap<String, String>();
	        if(parameters != null && !parameters.isEmpty()){
	        	Iterator it = parameters.keySet().iterator();
	        	while(it.hasNext()){
	        		String key= (String)it.next();
	        		parType.put(key, "STRING");
	        	}	        	
	        	query = StringUtilities.substituteParametersInString(query, parameters, parType, false);
	        }
	        statement.execute(query);
	        ResultSet resultSet = statement.getResultSet();	
	        
	        
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            SourceBean results = new SourceBean("ROWS");
            SourceBean row;

            int rowno = 0;
            while(resultSet.next()) {
            	if(++rowno > 1000) break;            	
           	
            	row = new SourceBean("ROW");
            	
            	for (int i=1; i<=columnCount; i++) {                   
            		row.setAttribute(resultSetMetaData.getColumnLabel(i), (resultSet.getString(i)==null)?"":resultSet.getString(i));
                }
            	results.setAttribute(row);         	
            }
            xml = results.toXML();

		 } catch (Exception ex) {
			 logger.error("Error while executing query", ex);
			 throw new Exception("Unable to execute query.");
	     } finally {
	       if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}  
	       }
	     }
	     return xml;
	}
	
	private static boolean canExecute(String query){
		boolean can = true;
		String lowerCaseQ = query.toLowerCase();
		lowerCaseQ = lowerCaseQ.trim();
		if(lowerCaseQ != null && lowerCaseQ.startsWith("update ") || lowerCaseQ.startsWith("drop ") || lowerCaseQ.startsWith("delete ")){
			return false;
		}
		return can;
	}

}
