package UnitTests;

import database.DataBase;
import jdk.nashorn.internal.parser.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.MockInjection;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import ucoach.data.model.*;
import ucoach.data.ws.*;

import javax.xml.ws.WebServiceContext;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.mockito.Matchers.any;

/**
 * Created by nikolai on 5/7/17.
 */

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@PrepareForTest({Authorization.class, GoogleTokens.class, User.class, GoogleTokensService.class})

public class GoogleTokensServiceTest {

    private GoogleTokensService solutionWithInvalidContext = new GoogleTokensService();
    private GoogleTokensService solutionWithValidContext = new GoogleTokensService();

    @Mock
    private WebServiceContext validContext;

    @Mock
    private WebServiceContext invalidContext;

    @Mock
    private User user;

    @Mock
    GoogleTokens googleTokens;

    @Mock
    private String accessToken;

    @Mock
    private String refreshToken;

    @Mock
    private Token token;

    @Before
    public void setUp() {
        Whitebox.setInternalState(solutionWithValidContext, "context", validContext);
        Whitebox.setInternalState(solutionWithInvalidContext, "context", invalidContext);

        PowerMockito.mockStatic(Authorization.class);
        PowerMockito.mockStatic(GoogleTokens.class);
        PowerMockito.mockStatic(User.class);
    }

    @Test
    public void getTokensTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.getTokens(0);
        PowerMockito.verifyStatic(Mockito.times(0));
        GoogleTokens.getTokensByUser(any(User.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(User.getUserById(0)).thenReturn(user);
        Mockito.when(GoogleTokens.getTokensByUser(user)).thenReturn(googleTokens);

        Assert.assertEquals(googleTokens, solutionWithValidContext.getTokens(0));
        PowerMockito.verifyStatic(Mockito.times(1));
        GoogleTokens.getTokensByUser(any(User.class));

    }

    @Test
    public void setGoogleTokens() throws Exception {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        Assert.assertEquals(null, solutionWithInvalidContext.setTokens(0, accessToken, refreshToken));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(User.getUserById(2)).thenReturn(null);

        Assert.assertEquals(null, solutionWithValidContext.setTokens(2, accessToken, refreshToken));

        Mockito.when(User.getUserById(0)).thenReturn(user);

        //GoogleTokens googleTokens1 = PowerMockito.mock(GoogleTokens.class);
        PowerMockito.whenNew(GoogleTokens.class).withNoArguments().thenReturn(googleTokens);
        PowerMockito.when(googleTokens.create()).thenReturn(googleTokens);


        Assert.assertEquals(googleTokens, solutionWithValidContext.setTokens(0, accessToken, refreshToken));
    }

    @Test
    public void updateTokensTest() throws Exception {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        Assert.assertEquals(null, solutionWithInvalidContext.updateTokens(0, accessToken));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(User.getUserById(2)).thenReturn(null);

        Assert.assertEquals(null, solutionWithValidContext.updateTokens(2, accessToken));

        Mockito.when(User.getUserById(0)).thenReturn(user);

        Mockito.when(GoogleTokens.getTokensByUser(user)).thenReturn(googleTokens);
        Mockito.when(googleTokens.update()).thenReturn(googleTokens);


        Assert.assertEquals(googleTokens, solutionWithValidContext.updateTokens(0, accessToken));
    }
}