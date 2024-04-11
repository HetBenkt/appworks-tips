package nl.bos;

import com.eibus.applicationconnector.uddi.IHTTPObject;
import com.eibus.applicationconnector.uddi.Interceptor;
import com.eibus.applicationconnector.uddi.InterceptorException;
import com.eibus.applicationconnector.uddi.UserInfo;
import com.eibus.util.logger.CordysLogger;
import com.eibus.xml.nom.Document;
import com.eibus.xml.nom.Node;
import com.eibus.xml.nom.XMLException;
import com.eibus.xml.xpath.XPath;
import com.eibus.xml.xpath.XPathMetaInfo;

import java.io.UnsupportedEncodingException;

public class MyInterceptor implements Interceptor {
    private static final CordysLogger LOGGER = CordysLogger.getCordysLogger(MyInterceptor.class);
    private XPathMetaInfo metaInfo = new XPathMetaInfo();

    @Override
    public void initialize(int args) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(args);
        }
        metaInfo.addNamespaceBinding("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
    }

    @Override
    public void onRequest(int request) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(request);
        }

        int soapheaderNode = XPath.getXPathInstance("//soapenv:Header").firstMatch(request, metaInfo);
        if (soapheaderNode > 0) {
            Node.delete(soapheaderNode);
        }

        Document doc = new Document();
        try {
            int wsSecurityHeaderNode = doc.parseString("<soapenv:Header xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"></soapenv:Header>");
            Node.insert(wsSecurityHeaderNode, Node.getFirstChild(request));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(request);
            }
        } catch (UnsupportedEncodingException | XMLException e) {
            LOGGER.error(e, null, doc);
        }
    }

    @Override
    public void onResponse(int response) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(response);
        }
    }

    @Override
    public void onSOAPFault(int soapFault) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(soapFault);
        }
    }

    @Override
    public void onHttpRequest(IHTTPObject httpObject, UserInfo userInfo) throws InterceptorException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(userInfo.getUserDN());
        }
    }

    @Override
    public void onHttpResponse(IHTTPObject httpObject, UserInfo userInfo) throws InterceptorException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(userInfo.getUserDN());
        }
    }
}