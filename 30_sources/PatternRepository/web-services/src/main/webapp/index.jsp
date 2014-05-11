<%-- 
    Document   : index
    Created on : 11.01.2010, 12:10:29
    Author     : cm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>OPR REST Web Services</title>
    </head>
    <body>
        <h1>OPR REST Web Services</h1>

        <h2>Types</h2>
        <p>
        <a href="OPRWebServiceTypes.xsd">XML Schema File</a><br />
        <small>This file can be used to generate objects that can be used with this service. (see also JAX-B, xjc command line tool)</small>
        </p>

        <h2>Resources</h2>
        <ul>
            <li>Pattern</li>
            
                <ul>
                    <li><b>GET</b> /resources/pattern/{id}<br /><small><a href="resources/pattern/1">/resources/pattern/1</a></small></li>
                    <li><b>GET</b> /resources/pattern/wiki/{wikiName}<br /><small><a href="resources/pattern/wiki/WizardPattern">/resources/pattern/wiki/WizardPattern</a></small></li>
                </ul>
            
            <li>Search</li>
            
                <ul>
                    <li><b>GET</b> /resources/search?q={query}<br /><small><a href="resources/search?q=pattern OR wizard">/resources/search?q=pattern OR wizard</a></small></li>
                </ul>
        </ul>
    </body>
</html>
