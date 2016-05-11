/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.engine.mobile.util;

import it.eng.spagobi.commons.filters.SpagoBICoreCheckSessionFilter;
import it.eng.spagobi.utilities.service.JSONSuccess;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


public class SpagoBIMobileCheckSessionFilter extends SpagoBICoreCheckSessionFilter{

	
	private static transient Logger logger = Logger.getLogger(SpagoBIMobileCheckSessionFilter.class);
	@Override
	protected String getSessionExpiredUrl() {
		// TODO Auto-generated method stub
		return "/WEB-INF/jsp/sessionExpired.jsp";
	}

    
}
