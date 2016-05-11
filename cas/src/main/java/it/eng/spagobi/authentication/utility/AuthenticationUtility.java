/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.authentication.utility;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFInternalError;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Giachino (antonella.giachino@eng.it)
 **/


public class AuthenticationUtility{
	protected static Logger logger = Logger.getLogger(AuthenticationUtility.class);
	private static final String PROPERTIES_FILE = "cas_spagobi.properties";
	public static Properties properties = null;
	
	//constructor
	public AuthenticationUtility() {
		logger.debug("IN");
		properties = loadProperties();
		logger.debug("OUT"); 
	}
	
	  /**
     * Get the connection using jdbc.
     * 
     * @return Connection to database
     * 
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException the SQL exception
    */
    private Connection getDirectConnection() throws ClassNotFoundException, SQLException {
    	Connection connection = null;
		String driver = "";
		String url = "";
		String user = "";
		String password = "";		

		driver = properties.getProperty("spagobi.datasource.driver");
		url = properties.getProperty("spagobi.datasource.url");
		user = properties.getProperty("spagobi.datasource.user");
		password = properties.getProperty("spagobi.datasource.pwd");
		
		if (driver == null || url == null || user == null){
			logger.error("Driver, url or user is null. Driver : " + driver + " - url: " + url + " - user: "+ user +
					"- \n Is impossible to connect to db for check user.");
			return null;
		}
		
    	
    	try {
    	    Class.forName(driver);
    	    
    	    connection = DriverManager.getConnection(url, user, password);
    	} catch (ClassNotFoundException e) {
    	    logger.error("Driver not found", e);
    	} catch (SQLException e) {
    	    logger.error("Cannot retrive connection", e);
    	}finally{
    		logger.debug("OUT");
    	}
    	return connection;
    } 
    
    /**
     * Execute the query and return a list of objects.
     * 
     * @param stmtName the statement name to execute for recovery data
     * @param pars list of parameters
     * 
     * @return list with data
     */
    public List executeQuery(String query, List pars) 
    	throws SQLException, EMFInternalError{
		
		if (query == null){
			logger.error("Statement is null. Is impossible to execute query.");
			return null;
		}
		
		Connection connection = null;
		ResultSet res = null;
		PreparedStatement pstmt = null;

		List lstValues = new ArrayList();
		try {
			connection = getDirectConnection();
			
			pstmt = connection.prepareStatement(query);
		
	        if (pars != null){
				for (int i=0; i< pars.size(); i++){
					pstmt.setString(i+1, ((String)pars.get(i))); 
				}
			}
	        
	       res = pstmt.executeQuery();

		   ResultSetMetaData rsm = res.getMetaData();

	       int columnCount = rsm.getColumnCount();
	       SourceBean sbRoot = new SourceBean("ROWS");
	       while (res.next()){
	    	   SourceBean sbRow = new SourceBean("ROW");
	           for (int j=0;j< columnCount;j++){
	        	 Object value = (res.getObject(j+1)==null)?"":res.getObject(j+1);
	        	 sbRow.setAttribute(rsm.getColumnName(j+1), value);
	           }
	           sbRoot.setAttribute(sbRow);
	        }
	        
			lstValues = sbRoot.getContainedAttributes();
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			
			try{
				
			    if (res!=null)
			        res.close();
			    
			    if (pstmt!=null)
			    	pstmt.close();
			    
			    if (!(connection==null))
			        connection.close();
			      
			}catch (Exception e){
				logger.error("Error while getting user data " + e);
		        throw new EMFInternalError("ERROR", "cannot getting user data ");
			}
		      
		}
		return lstValues;
    }

    /**
     * Execute the update and return a boolean with the result.
     * 
     * @param stmtName the statement name to execute for updating data
     * @param pars list of parameters
     * 
     * @return boolean
     */
    public Integer executeUpdate(String query, List pars) 
    	throws SQLException, EMFInternalError{
		
		if (query == null){
			logger.error("Statement is null. Is impossible to execute query.");
			return null;
		}
		
		Connection connection = null;
		int res = 0;
		PreparedStatement pstmt = null;

		try {
			connection = getDirectConnection();
			pstmt = connection.prepareStatement(query);
		
	        if (pars != null){
				for (int i=0; i< pars.size(); i++){
					pstmt.setString(i+1, ((String)pars.get(i))); 
				}
			}
	        
	       res = pstmt.executeUpdate();
	       logger.debug("Result of executeUpdate instruction: " + res);
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			
			try{
			    
			    if (pstmt!=null)
			    	pstmt.close();
			    
			    if (!(connection==null))
			        connection.close();
			      
			}catch (Exception e){
				logger.error("Error while setting user data " + e);
		        throw new EMFInternalError("ERROR", "cannot setting user data ");
			}
		      
		}
		return Integer.valueOf(res);
    }
    
    private Properties loadProperties() {
		logger.debug("IN");
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
		 	Properties props = new Properties();
		 	if (is != null) props.load(is);
			return props;
		} catch (Exception e) {
			logger.error("Error while loading prompts association file: " + e.getMessage());
			return new Properties();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					logger.debug("Error while closing stream on " + PROPERTIES_FILE + " file.");
				}
			logger.debug("OUT");
		}
	}

}