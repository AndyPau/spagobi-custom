/* Copyright 2012 Engineering Ingegneria Informatica S.p.A. – SpagoBI Competency Center
 * The original code of this file is part of Spago java framework, Copyright 2004-2007.

 * This Source Code Form is subject to the term of the Mozilla Public Licence, v. 2.0. If a copy of the MPL was not distributed with this file, 
 * you can obtain one at http://Mozilla.org/MPL/2.0/.

 * Alternatively, the contents of this file may be used under the terms of the LGPL License (the “GNU Lesser General Public License”), in which 
 * case the  provisions of LGPL are applicable instead of those above. If you wish to  allow use of your version of this file only under the 
 * terms of the LGPL  License and not to allow others to use your version of this file under  the MPL, indicate your decision by deleting the 
 * provisions above and  replace them with the notice and other provisions required by the LGPL.  If you do not delete the provisions above, 
 * a recipient may use your version  of this file under either the MPL or the GNU Lesser General Public License. 

 * Spago is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or any later version. Spago is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License 
 * for more details.
 * You should have received a copy of the GNU Lesser General Public License along with Spago. If not, see: http://www.gnu.org/licenses/. The complete text of 
 * Spago license is included in the  COPYING.LESSER file of Spago java framework.
 */
package it.eng.spago.navigation;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.tracing.TracerSingleton;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// TODO: Auto-generated Javadoc
/**
 * The Class LightNavigationManager.
 */
public class LightNavigationManager {

    /** The Constant LIGHT_NAVIGATOR_RESET. */
    public static final String LIGHT_NAVIGATOR_RESET = "LIGHT_NAVIGATOR_RESET";
    
    /** The Constant LIGHT_NAVIGATOR_DISABLED. */
    public static final String LIGHT_NAVIGATOR_DISABLED = "LIGHT_NAVIGATOR_DISABLED";
    
    /** The Constant LIGHT_NAVIGATOR_BACK_TO. */
    public static final String LIGHT_NAVIGATOR_BACK_TO = "LIGHT_NAVIGATOR_BACK_TO";
    
    /** The Constant LIGHT_NAVIGATOR_REPLACE_LAST. */
    public static final String LIGHT_NAVIGATOR_REPLACE_LAST = "LIGHT_NAVIGATOR_REPLACE_LAST";
    
    /** The Constant LIGHT_NAVIGATOR_BACK_TO_MARK. */
    public static final String LIGHT_NAVIGATOR_BACK_TO_MARK = "LIGHT_NAVIGATOR_BACK_TO_MARK";
    
    /** The Constant LIGHT_NAVIGATOR_MARK. */
    public static final String LIGHT_NAVIGATOR_MARK = "LIGHT_NAVIGATOR_MARK";

    /**
     * Control light navigation.
     * 
     * @param request the request
     * @param serviceRequest the service request
     * 
     * @return the source bean
     * 
     * @throws Exception the exception
     */
    public static SourceBean controlLightNavigation (PortletRequest request, SourceBean serviceRequest) throws Exception {
    	Object o = (Object)request;    	
    	return controlLightNavigation(o, serviceRequest);
    }
	
    /**
     * This method is responsible for the requests stack (the <code>LightNavigator</code>) modification.
     * It controls if there is one of the following attributes in the service request:
     * <code>LIGHT_NAVIGATOR_RESET</code> (requests stack is resetted);
     * <code>LIGHT_NAVIGATOR_DISABLED</code> (the navigator is disabled: the stack is not modified and the original request is returned);
     * <code>LIGHT_NAVIGATOR_BACK_TO</code> (the request in the stack at position represented by this attribute is returned);
     * <code>LIGHT_NAVIGATOR_BACK_TO_MARK</code> (the request in the stack marked by the string represented by this attribute is returned);
     * <code>LIGHT_NAVIGATOR_MARK</code> (the request at input is marked with the string represented by this attribute and put in the stack).
     * <code>LIGHT_NAVIGATOR_REPLACE_LAST</code> (the more recent request is replaced by the request at input).
     * If any errors occur, the original request is returned.
     * 
     * @param serviceRequest The original service request <code>SourceBean</code>
     * @param request the request
     * 
     * @return the service request <code>SourceBean</code>
     * 
     * @throws Exception the exception
     */
	public static SourceBean controlLightNavigation (Object request, SourceBean serviceRequest) throws Exception {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "Method controlNavigation in LightNavigationManager class invoked with service request:\n" + serviceRequest);
		
		if (request == null) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "LightNavigationManager: " +
					"controlLightNavigation: request object at input is null.");
			throw new Exception ("request object is null.");
		}
		if (serviceRequest == null) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "LightNavigationManager: " +
					"controlLightNavigation: the service request SourceBean at input is null.");
			throw new Exception ("Service request SourceBean is null.");
		}
		
		// Makes a clone of the serviceRequest, to be returned in case of errors
		SourceBean savedServiceRequest = (SourceBean) serviceRequest.cloneObject();
		
		// Retrieves the LightNavigator object from session; if it does not exist it creates a new one
		LightNavigator lightNavigator = retrieveLightNavigatorFromSession(request);
		
		try {
			
	        // If LightNavigator is disabled return the serviceRequest unmodified
			String lightNavigatorDisabled = (String) serviceRequest.getAttribute(LIGHT_NAVIGATOR_DISABLED);
			if ("true".equalsIgnoreCase(lightNavigatorDisabled)) {
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LightNavigator disabled; " +
						"return from the method controlNavigation without any modifications to the original request.");
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "The requests stack is not modified:\n" + lightNavigator.toString());
				return serviceRequest;
			}

			String lightNavigatorBackTo = (String) serviceRequest.getAttribute(LIGHT_NAVIGATOR_BACK_TO);
			if (lightNavigatorBackTo != null) {
				int position = Integer.parseInt(lightNavigatorBackTo);
				MarkedRequest markedRequest = lightNavigator.goBackToPosition(position);
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LightNavigationManager: controlLightNavigation: " +
						"returning request at position " + position + " of the stack.");
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "The requests stack now is:\n" + lightNavigator.toString());
				return markedRequest.getRequest();
			}
			
			String lightNavigatorReplaceLast = (String) serviceRequest.getAttribute(LIGHT_NAVIGATOR_REPLACE_LAST);
			if (lightNavigatorReplaceLast != null) {
				String mark = (String) serviceRequest.getAttribute(LIGHT_NAVIGATOR_MARK);
				MarkedRequest markedRequest = new MarkedRequest(serviceRequest, mark);
				lightNavigator.replaceLast(markedRequest);
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LightNavigationManager: controlLightNavigation: " +
						"substituted the more recent request (at position 0) of the stack with the input one.");
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "The requests stack now is:\n" + lightNavigator.toString());
				return markedRequest.getRequest();
			}
			
			String lightNavigatorBackToMark = (String) serviceRequest.getAttribute(LIGHT_NAVIGATOR_BACK_TO_MARK);
			if (lightNavigatorBackToMark != null) {
				MarkedRequest markedRequest = lightNavigator.goBackToMark(lightNavigatorBackToMark);
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LightNavigationManager: controlLightNavigation: " +
						"returning request with mark " + lightNavigatorBackToMark + " of the stack.");
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "The requests stack now is:\n" + lightNavigator.toString());
				return markedRequest.getRequest();
			}
			
			String lightNavigatorReset = (String) serviceRequest.getAttribute(LIGHT_NAVIGATOR_RESET);
			if (lightNavigatorReset != null) {
				lightNavigator.reset();
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LightNavigationManager: controlLightNavigation: " +
						"stack reset executed.");
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "The requests stack now is empty.");
				return serviceRequest;
			}
			
			String mark = (String) serviceRequest.getAttribute(LIGHT_NAVIGATOR_MARK);
			MarkedRequest markedRequest = new MarkedRequest(serviceRequest, mark);
			lightNavigator.add(markedRequest);
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "LightNavigationManager: controlLightNavigation: " +
					"putting the request in the stack " + 
					(mark == null ? "without mark" : "with mark '" + mark + "'") + ". Returning unmodified request.");
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "The requests stack now is:\n" + lightNavigator.toString());
			return serviceRequest;
			
		} catch (Exception e) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.MAJOR, "LightNavigationManager: " +
					"controlLightNavigation: an exception occurred. " +
					"Returning the original request.", e);
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "The requests stack now is:\n" + 
					lightNavigator.toString());
			return savedServiceRequest;
		}

	}
	
	/**
	 * Retrieves the <code>LightNavigator</code> object from session; if it does not exist it creates a new one.
	 * 
	 * @param request the request
	 * 
	 * @return the light navigator
	 * 
	 * @throws Exception the exception
	 */
	private static LightNavigator retrieveLightNavigatorFromSession (Object request) throws Exception {
		LightNavigator lightNavigator = null;
		if(request instanceof PortletRequest) {
			PortletRequest portletRequest = (PortletRequest)request;
			PortletSession session = portletRequest.getPortletSession();
			if (session == null) {
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "LightNavigationManager: " +
					"controlLightNavigation: PortletSession object is null.");
				throw new Exception ("PortletSession object is null.");
			}
			Object lightNavigatorObj = session.getAttribute("LIGHT_NAVIGATOR");
			if (lightNavigatorObj != null) lightNavigator = (LightNavigator) lightNavigatorObj;
			else {
				lightNavigator = new LightNavigator();
				session.setAttribute("LIGHT_NAVIGATOR", lightNavigator);
			}
		} else if(request instanceof HttpServletRequest) {
			HttpServletRequest servRequest = (HttpServletRequest)request;
			HttpSession session = servRequest.getSession();
			if (session == null) {
				TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "LightNavigationManager: " +
					"controlLightNavigation: HttpSession object is null.");
				throw new Exception ("HttpSession object is null.");
			}
			Object lightNavigatorObj = session.getAttribute("LIGHT_NAVIGATOR");
			if (lightNavigatorObj != null) lightNavigator = (LightNavigator) lightNavigatorObj;
			else {
				lightNavigator = new LightNavigator();
				session.setAttribute("LIGHT_NAVIGATOR", lightNavigator);
			}
		} else {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "LightNavigationManager: " +
								"controlLightNavigation: request object is neither a PortletRequest " +
								"nor an HttpServletRequest");
			throw new Exception ("request object is neither a PortletRequest nor an HttpServletRequest");
		}
		return lightNavigator;
	}
	
}
