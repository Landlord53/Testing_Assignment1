package ucoach.data.Mocks;

/**
 * Created by nikolai on 5/7/17.
 */
import java.security.Principal;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.w3c.dom.Element;

/**
 * Mock implementation of WebServiceContext to be used in tests
 * @author victor.starenky
 *
 */
public class MockWebServiceContext implements WebServiceContext
{
    /**
     * principal
     */
    private Principal userPrincipal;

    /**
     * @see javax.xml.ws.WebServiceContext#getMessageContext()
     */

    public MessageContext getMessageContext()
    {
        throw new RuntimeException("not implemented! I'm not a real WebServiceContext unfortunately... ");
        //MessageContext

        //return mp;
    }

    /**
     * @see javax.xml.ws.WebServiceContext#getUserPrincipal()
     */
    public Principal getUserPrincipal()
    {
        return userPrincipal;
    }

    /**
     * set principal
     * @param newPrincipal
     */
    public void setUserPrincipal(Principal newPrincipal) {
        userPrincipal = newPrincipal;
    }

    /**
     * @see javax.xml.ws.WebServiceContext#isUserInRole(java.lang.String)
     */
    public boolean isUserInRole(String arg0)
    {
        throw new RuntimeException("not implemented! I'm not a real WebServiceContext unfortunately... ");
    }

    /**
     * @param username
     */
    public MockWebServiceContext(String username)
    {
        super();
        this.userPrincipal = new MockPrincipal(username);

    }

    /**
     * @param referenceParameters
     * @return EndpointReference
     */
    public EndpointReference getEndpointReference(
            Element... referenceParameters)
    {
        throw new RuntimeException("method not implemented");
    }

    /**
     * @param clazz
     * @param referenceParameters
     * @param <T>
     * @return endpoint reference
     */
    public <T extends EndpointReference> T getEndpointReference(Class<T> clazz,
                                                                Element... referenceParameters)
    {
        throw new RuntimeException("method not implemented");
    }



}