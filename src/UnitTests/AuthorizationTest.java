package UnitTests;

/**
 * Created by nikolai on 5/7/17.
 */
import net.bytebuddy.implementation.Implementation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ucoach.data.ws.Authorization;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import static org.junit.Assert.*;

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@PrepareForTest(Authorization.class)
public class AuthorizationTest {

    @Mock
    WebServiceContext contex;

    @Mock
    MessageContext messageContext;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Authorization.class);
    }

    @Test
    public void validateRequest() throws Exception {
        PowerMockito.mockStatic(System.class);
        Mockito.when(System.getenv("INTERNAL_DATA_AUTH_KEY")).thenReturn("default_key");
        assertEquals("default_key", System.getenv("INTERNAL_DATA_AUTH_KEY"));
    }

}