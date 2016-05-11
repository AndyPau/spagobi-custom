<%-- SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.  If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/. --%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript" src="js/sbisdk-all-production.js"></script>
	<!--  script type="text/javascript" src="http://localhost:8080/SpagoBI/js/src/sdk/sbisdk-all-production.js"></script -->

	<script type="text/javascript">

		Sbi.sdk.services.setBaseUrl({
	        protocol: 'http'     
	        , host: 'localhost'
	        , port: '8080'
	        , contextPath: 'SpagoBI' 
	    });
		
 		execTest9 = function() {
 			Sbi.sdk.api.injectQbe({
				datasetLabel: 'DS_DEMO_51_COCKPIT'
 				, target: 'qbe'
				, height: '600px'
				, width: '1100px'
				, iframe: {
					style: 'border: 0px;'
				}
			});
		};
	</script>
</head>


<body>
<h2>Example 9 : injectQbe into non-existing div</h2>
<hr>
<b>Description:</b> Use <i>injectQbe</i> function to inject into a div an html string that contains the definition of an iframe 
pointing to the url of qbe start action. In this example the specified target div does not exist so it is created on the fly by the function
	
<p>
<b>Code: </b>
<p>
<BLOCKQUOTE>
<PRE>
execTest9 = function() {
	Sbi.sdk.api.injectQbe({
		datasetLabel: 'DS_DEMO_51_COCKPIT'
		, target: 'qbe'
		, height: '600px'
		, width: '1100px'
		, iframe: {
			style: 'border: 0px;'
		}
	});
};
</PRE>
</BLOCKQUOTE>
<hr>
<script type="text/javascript">
	execTest9();
</script>
</body>
</html>