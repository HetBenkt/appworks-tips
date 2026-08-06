package nl.bos.http;

import com.eibus.security.identity.Identity;
import com.eibus.util.logger.CordysLogger;
import com.eibus.xml.nom.Document;
import com.eibus.xml.nom.Node;
import com.eibus.xml.nom.XMLException;
import com.opentext.applicationconnector.httpconnector.IHTTPObject;
import com.opentext.applicationconnector.httpconnector.config.IServerConnection;
import com.opentext.applicationconnector.httpconnector.exception.HandlerException;
import com.opentext.applicationconnector.httpconnector.impl.StandardRequestHandler;

import java.net.HttpURLConnection;

public class MyStandardRequestHandler extends StandardRequestHandler {
    private static final CordysLogger LOG = CordysLogger.getCordysLogger(MyStandardRequestHandler.class);

    @Override
    public IHTTPObject process(int requestNode, IServerConnection connection, Identity identity) throws HandlerException {
        String SAMLart = Node.getData(requestNode).substring(Node.getData(requestNode).indexOf("<samlart>")+9, Node.getData(requestNode).indexOf("</samlart>"));
        String SoapEnvelope = Node.getData(requestNode).substring(Node.getData(requestNode).indexOf("</samlart>")+11, Node.getData(requestNode).length());

        Document document = new Document();
        int soapEnvelopeNode = 0;
        try {
            soapEnvelopeNode = document.load(SoapEnvelope.getBytes());
        } catch (XMLException e) {
            throw new HandlerException(e);
        }

        if(LOG.isDebugEnabled()) {
            LOG.debug(String.format("MyStandardRequestHandler.process() for user: %s with SAMLart: %s", identity.getAuthenticatedUserCN(), SAMLart));
        }

        IHTTPObject process = super.process(soapEnvelopeNode, connection, identity);
        HttpURLConnection httpUrlConnection = process.getHttpUrlConnection();
        httpUrlConnection.setRequestProperty("SAMLart", SAMLart);

        return process;
    }
}