/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.spagobi.sdk.engines.stub;

public class EnginesServiceServiceLocator extends org.apache.axis.client.Service implements it.eng.spagobi.sdk.engines.stub.EnginesServiceService {

    public EnginesServiceServiceLocator() {
    }


    public EnginesServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EnginesServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EnginesService
    private java.lang.String EnginesService_address = "http://localhost:8080/SpagoBI/sdk/EnginesService";

    public java.lang.String getEnginesServiceAddress() {
        return EnginesService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EnginesServiceWSDDServiceName = "EnginesService";

    public java.lang.String getEnginesServiceWSDDServiceName() {
        return EnginesServiceWSDDServiceName;
    }

    public void setEnginesServiceWSDDServiceName(java.lang.String name) {
        EnginesServiceWSDDServiceName = name;
    }

    public it.eng.spagobi.sdk.engines.stub.EnginesService getEnginesService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EnginesService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEnginesService(endpoint);
    }

    public it.eng.spagobi.sdk.engines.stub.EnginesService getEnginesService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.eng.spagobi.sdk.engines.stub.EnginesServiceSoapBindingStub _stub = new it.eng.spagobi.sdk.engines.stub.EnginesServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getEnginesServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEnginesServiceEndpointAddress(java.lang.String address) {
        EnginesService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.eng.spagobi.sdk.engines.stub.EnginesService.class.isAssignableFrom(serviceEndpointInterface)) {
                it.eng.spagobi.sdk.engines.stub.EnginesServiceSoapBindingStub _stub = new it.eng.spagobi.sdk.engines.stub.EnginesServiceSoapBindingStub(new java.net.URL(EnginesService_address), this);
                _stub.setPortName(getEnginesServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("EnginesService".equals(inputPortName)) {
            return getEnginesService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:spagobisdkengines", "EnginesServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:spagobisdkengines", "EnginesService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EnginesService".equals(portName)) {
            setEnginesServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
