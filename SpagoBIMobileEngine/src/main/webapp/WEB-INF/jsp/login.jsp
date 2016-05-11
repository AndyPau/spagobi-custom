<%-- SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.  If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/. --%>

<%-- 
authors: Monica Franceschini
		 Davide Zerbetto

--%>

<%@page import="it.eng.spago.base.SessionContainer"%>
<%@page import="it.eng.spago.base.RequestContainer"%>
<%@page import="it.eng.spago.security.IEngUserProfile"%>
<%@page import="it.eng.spagobi.commons.bo.UserProfile"%>
<%@page import="java.util.Locale"%>
<%@ page language="java" 
	     contentType="text/html; charset=UTF-8" 
	     pageEncoding="UTF-8"
	     import="it.eng.spagobi.commons.SingletonConfig"%>

<%
Locale locale=new Locale("en","US");

SingletonConfig serverConfig = SingletonConfig.getInstance();
String roleToCheckLbl  =  SingletonConfig.getInstance().getConfigValue("SPAGOBI.SECURITY.ROLE_LOGIN");
String roleToCheckVal = "";
if (!("").equals(roleToCheckLbl)){
	roleToCheckVal = (request.getParameter(roleToCheckLbl)!=null)?request.getParameter(roleToCheckLbl):"";	
}
RequestContainer reqCont = RequestContainer.getRequestContainer();
SessionContainer sessCont = reqCont.getSessionContainer();
SessionContainer permSess = sessCont.getPermanentContainer();
UserProfile previousProfile = (UserProfile) permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
boolean logged = (previousProfile!=null);
%>

<html>

	<head>
	

		<%@ include file="/WEB-INF/jsp/includeMessageResource.jspf" %>
		<%@ include file="/WEB-INF/jsp/importSenchaJSLibrary.jspf" %>
		<%@ include file="/WEB-INF/jsp/constants.jspf" %>
		<%@ include file="/WEB-INF/jsp/env.jspf" %>
		

		
		<%@ include file="/WEB-INF/jsp/importSbiJS.jspf" %>
		
		<script type="text/javascript">	

			    
		function changeHashOnLoad() {
		            window.location.href += "#";
		            setTimeout("changeHashAgain()", "50");
		        }
		        function changeHashAgain() {
		            window.location.href += "1";
		        }
		        var storedHash = window.location.hash;
		        window.setInterval(function() {
		        if (window.location.hash != storedHash) 
		        { window.location.hash = storedHash; } }, 50);
		
		</script>	
		
	</head>

	<body onload ="changeHashOnLoad();">

		 <script>
		 

		 	
		 	var ajaxReqGlobalTimeout = 120000;
			var hostGlobal= '<%= request.getServerName()%>';
			var portGlobal= '<%= request.getServerPort()%>';
			var roleGlobal = '<%=roleToCheckVal%>';
			var loggedGlobal =  '<%=logged%>';
		 </script>
	</body>
 
</html>