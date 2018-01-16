package com.movilizer.jc;

import org.jibx.ws.soap.client.SoapClient;

import com.movilitas.jaxbframework.MovilizerJaxbException;
import com.movilitas.jaxbframework.dataaccess.jaxb.impl.JaxbDAOMovilizerImpl;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerRequest;
import com.movilitas.jaxbframework.dataaccess.jaxb.v14.MovilizerResponse;

public class MovilizerWS
{
    private static final int     VERSION         = 14;
    private static final int     SYSTEM_ID       = 10001;
    private static final String  SYSTEM_PASSWORD = "1";
    private static final String  URL             = "http://demo.movilizer.com/MovilizerDistributionService/WebService/";

    private boolean              _debug          = false;

    private JaxbDAOMovilizerImpl jaxbDAO;

    public MovilizerWS() throws MovilizerJaxbException
    {
        this.jaxbDAO = new JaxbDAOMovilizerImpl();
    }

    public MovilizerResponse fireEmptyRequest(final String ackKey, final String queue) throws Exception
    {
        final MovilizerRequest movilizerRequest = new MovilizerRequest();
        movilizerRequest.setSystemId(SYSTEM_ID);
        movilizerRequest.setSystemPassword(SYSTEM_PASSWORD);
        movilizerRequest.setResponseQueue(queue);

        if (ackKey != null)
        {
            movilizerRequest.setRequestAcknowledgeKey(ackKey);
        }
        movilizerRequest.setNumResponses(new Integer(100));

        return sendRequest(movilizerRequest);
    }

    public MovilizerResponse sendRequest(final MovilizerRequest movilizerRequest) throws Exception
    {
        movilizerRequest.setSystemId(SYSTEM_ID);
        movilizerRequest.setSystemPassword(SYSTEM_PASSWORD);

        if (this._debug)
        {
            System.out.println("Generated: \n " + this.jaxbDAO.generateXML(movilizerRequest));
        }

        final SoapClient soapClient = this.jaxbDAO.createWebServiceDispatcher(URL, VERSION);
        final MovilizerResponse response = (MovilizerResponse) soapClient.call(movilizerRequest);

        if (this._debug)
        {
            System.out.println("Received: \n" + this.jaxbDAO.generateXML(response));
        }

        return response;
    }

    public String toXML(final Object obj) throws MovilizerJaxbException
    {
        return this.jaxbDAO.generateXML(obj);
    }

    public boolean isDebug()
    {
        return this._debug;
    }

    public void setDebug(final boolean debug)
    {
        this._debug = debug;
    }
}
