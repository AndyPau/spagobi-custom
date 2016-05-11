/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.jpivotaddins.util;

import it.eng.spagobi.utilities.ParametersDecoder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Node;


public class ParameterUtilities {
	
	
	public static String substituteQueryParameters(String queryStr, List parameters, HttpServletRequest request) {
		String newQuery = queryStr;
		if (parameters != null && parameters.size() > 0) {
	    	for (int i = 0; i < parameters.size(); i++) {
	    		//update the query if there is more than one parameter (else only the last is correctly settled)
	    		if(i>0){
	    			queryStr = newQuery;
	    		}
	    		Node parameter = (Node) parameters.get(i);
	    		String name = "";
	    		String as = "";
	    		if (parameter != null) {
	    			name = parameter.valueOf("@name");
	    			as = parameter.valueOf("@as");
	    		}
	    		String parameterValue = request.getParameter(name);
	    		if((parameterValue==null) || parameterValue.trim().equals("") ){
	    			continue;
	    		}
	    		String decodedParameterValue = parameterValue;
	    		ParametersDecoder decoder = new ParametersDecoder();
	    		if(decoder.isMultiValues(parameterValue)) {
	    			decodedParameterValue = (String)decoder.decode(parameterValue).get(0);
	    		}
				newQuery = ParameterUtilities.setParameters(queryStr, as, parameterValue);				
	    	}
	    }		
	    return newQuery;
	}
	
	
	public static String setParameters(String query, String pname, String pvalue) {
		String newQuery = query;
		// substitute the mondrian parameter sintax		
		int index = -1;
		int ptr = 0;
		while( (index = newQuery.indexOf("Parameter", ptr)) != -1 ) {
			ptr = newQuery.indexOf("(", index);
			String firstArg = newQuery.substring(newQuery.indexOf("(", ptr) + 1, newQuery.indexOf(",", ptr));	
			if(!firstArg.trim().equalsIgnoreCase("\""+pname+"\"")) 
				continue;
			ptr = newQuery.indexOf(",", ptr) + 1; // 2 arg
			String secondArg = newQuery.substring(ptr, newQuery.indexOf(",", ptr));	
			ptr = newQuery.indexOf(",", ptr) + 1; // 3 arg
			String thirdArg = newQuery.substring(ptr, newQuery.indexOf(",", ptr));
			// if the parameter type is STRING, add double apix to the value passed by SpagoBI
			if(secondArg.equalsIgnoreCase("STRING")){
				newQuery = newQuery.substring(0, ptr) + '"' + pvalue + '"' + newQuery.substring(newQuery.indexOf(",", ptr+1), newQuery.length());
			} else {
				newQuery = newQuery.substring(0, ptr) + pvalue + newQuery.substring(newQuery.indexOf(",", ptr+1), newQuery.length());
			}
		}
		// substitute the spagobi parameter sintax 
		index = -1;
		ptr = 0;
		while((index=newQuery.indexOf("${", ptr)) != -1 ) {
			int indexEnd = newQuery.indexOf("}", index);
			ptr = indexEnd;
			String namePar = newQuery.substring(index+2, indexEnd);
			
			// TODO manage property parameters type
			// If the parameter comes from a property, a double apix has to be added
			// but we have to pay attention to recognize property parameters and filter 
			// conditions
			
			if(!namePar.trim().equalsIgnoreCase(pname)) 
				continue;
			newQuery = newQuery.substring(0, index) + 
					   pvalue + 
					   newQuery.substring(indexEnd+1, newQuery.length());	
		}
		// return query
		return newQuery;
	}
}
