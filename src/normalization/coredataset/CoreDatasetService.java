
package normalization.coredataset;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CoreDatasetService", targetNamespace = "http://CoreDataset", wsdlLocation = "https://kandel.dia.fi.upm.es:8443/CoreDatasetService2/services/CoreDatasetService?wsdl")
public class CoreDatasetService
    extends Service
{

    private final static URL COREDATASETSERVICE_WSDL_LOCATION;
    private final static WebServiceException COREDATASETSERVICE_EXCEPTION;
    private final static QName COREDATASETSERVICE_QNAME = new QName("http://CoreDataset", "CoreDatasetService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://kandel.dia.fi.upm.es:8443/CoreDatasetService2/services/CoreDatasetService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        COREDATASETSERVICE_WSDL_LOCATION = url;
        COREDATASETSERVICE_EXCEPTION = e;
    }

    public CoreDatasetService() {
        super(__getWsdlLocation(), COREDATASETSERVICE_QNAME);
    }

    public CoreDatasetService(WebServiceFeature... features) {
        super(__getWsdlLocation(), COREDATASETSERVICE_QNAME, features);
    }

    public CoreDatasetService(URL wsdlLocation) {
        super(wsdlLocation, COREDATASETSERVICE_QNAME);
    }

    public CoreDatasetService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, COREDATASETSERVICE_QNAME, features);
    }

    public CoreDatasetService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CoreDatasetService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CoreDatasetServicePortType
     */
    @WebEndpoint(name = "CoreDatasetServiceHttpsSoap11Endpoint")
    public CoreDatasetServicePortType getCoreDatasetServiceHttpsSoap11Endpoint() {
        return super.getPort(new QName("http://CoreDataset", "CoreDatasetServiceHttpsSoap11Endpoint"), CoreDatasetServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CoreDatasetServicePortType
     */
    @WebEndpoint(name = "CoreDatasetServiceHttpsSoap11Endpoint")
    public CoreDatasetServicePortType getCoreDatasetServiceHttpsSoap11Endpoint(WebServiceFeature... features) {
        return super.getPort(new QName("http://CoreDataset", "CoreDatasetServiceHttpsSoap11Endpoint"), CoreDatasetServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns CoreDatasetServicePortType
     */
    @WebEndpoint(name = "CoreDatasetServiceHttpsSoap12Endpoint")
    public CoreDatasetServicePortType getCoreDatasetServiceHttpsSoap12Endpoint() {
        return super.getPort(new QName("http://CoreDataset", "CoreDatasetServiceHttpsSoap12Endpoint"), CoreDatasetServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CoreDatasetServicePortType
     */
    @WebEndpoint(name = "CoreDatasetServiceHttpsSoap12Endpoint")
    public CoreDatasetServicePortType getCoreDatasetServiceHttpsSoap12Endpoint(WebServiceFeature... features) {
        return super.getPort(new QName("http://CoreDataset", "CoreDatasetServiceHttpsSoap12Endpoint"), CoreDatasetServicePortType.class, features);
    }

    /**
     * 
     * @return
     *     returns CoreDatasetServicePortType
     */
    @WebEndpoint(name = "CoreDatasetServiceHttpsEndpoint")
    public CoreDatasetServicePortType getCoreDatasetServiceHttpsEndpoint() {
        return super.getPort(new QName("http://CoreDataset", "CoreDatasetServiceHttpsEndpoint"), CoreDatasetServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CoreDatasetServicePortType
     */
    @WebEndpoint(name = "CoreDatasetServiceHttpsEndpoint")
    public CoreDatasetServicePortType getCoreDatasetServiceHttpsEndpoint(WebServiceFeature... features) {
        return super.getPort(new QName("http://CoreDataset", "CoreDatasetServiceHttpsEndpoint"), CoreDatasetServicePortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (COREDATASETSERVICE_EXCEPTION!= null) {
            throw COREDATASETSERVICE_EXCEPTION;
        }
        return COREDATASETSERVICE_WSDL_LOCATION;
    }

}
