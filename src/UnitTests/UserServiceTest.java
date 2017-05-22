package UnitTests;

import database.DataBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import ucoach.data.model.Coach;
import ucoach.data.model.User;
import ucoach.data.ws.Authorization;
import ucoach.data.ws.UserService;

import javax.xml.ws.WebServiceContext;

/**
 * Created by nikolai on 5/7/17.
 */

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@PrepareForTest({Authorization.class, User.class})

public class UserServiceTest {

    private UserService solutionWithInvalidContext = new UserService();
    private UserService solutionWithValidContext = new UserService();
    private DataBase dataBase = new DataBase();
    private User user;
    private User user2;

    @Mock
    private WebServiceContext validContext;

    @Mock
    private WebServiceContext invalidContext;

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(solutionWithValidContext, "context", validContext);
        Whitebox.setInternalState(solutionWithInvalidContext, "context", invalidContext);

        PowerMockito.mockStatic(User.class);
        PowerMockito.mockStatic(Authorization.class);

        user = new User();
        user.setPassword("12345678");
        user.setEmail("dog-1@mail.ru");
        user.setFirstname("Nikolay");
        user.setLastname("Chalykh");
        user.setId(0);

        user2 = new User();
        user2.setPassword("12345678");
        user2.setEmail("dog-2@mail.ru");
        user2.setFirstname("Dmitrii");
        user2.setLastname("Mironov");
        user2.setId(1);
    }

    @Test
    public void getUserTest() {
        dataBase.createUser(user);
        dataBase.createUser(user2);

        Mockito.when(User.getUserById(0)).thenReturn(dataBase.getUserById(0));
        Mockito.when(User.getUserById(1)).thenReturn(dataBase.getUserById(1));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);

        Assert.assertEquals(user, solutionWithValidContext.getUser(0));
        Assert.assertEquals(user2, solutionWithValidContext.getUser(1));
        Assert.assertEquals(null, solutionWithValidContext.getUser(2));

        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);

        Assert.assertEquals(null, solutionWithInvalidContext.getUser(0));

        dataBase.reset();
    }

    @Test
    public void createUserTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);

        Assert.assertEquals(null, solutionWithInvalidContext.createUser(user));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);

        Mockito.when(User.createUser(user)).thenReturn(dataBase.createUser(user));
        Mockito.when(User.createUser(user2)).thenReturn(dataBase.createUser(user2));

        Assert.assertEquals(2, dataBase.getUserNum());
        Assert.assertEquals(user, solutionWithValidContext.createUser(user));
        Assert.assertEquals(user2, solutionWithValidContext.createUser(user2));

        dataBase.reset();
    }

    @Test
    public void updateCoachTest() {
        dataBase.createUser(user);

        User newUser = new User();
        newUser.setFirstname("John");
        newUser.setLastname("Sinna");
        newUser.setId(1);

        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        Assert.assertEquals(null, solutionWithInvalidContext.updateUser(newUser));

        User userMustBeReturned = new User();
        userMustBeReturned.setId(1);
        userMustBeReturned.setFirstname("John");
        userMustBeReturned.setLastname("Sinna");
        userMustBeReturned.setPassword("12345678");
        userMustBeReturned.setEmail("dog-2@mail.ru");

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(User.updateUser(newUser)).thenReturn(dataBase.updateUser(userMustBeReturned));
        User updatedUser = solutionWithValidContext.updateUser(newUser);
        Assert.assertEquals(userMustBeReturned, updatedUser);

        dataBase.reset();
    }

    @Test
    public void getUserByEMailTest(){
        dataBase.createUser(user);

        Mockito.when(User.getUserByEmail("dog-1@mail.ru")).thenReturn(user);
        Mockito.when(User.getUserByEmail("dog-2@mail.ru")).thenReturn(user2);

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);

        Assert.assertEquals(user, solutionWithValidContext.getUserByEmail("dog-1@mail.ru"));
        Assert.assertEquals(user2, solutionWithValidContext.getUserByEmail("dog-2@mail.ru"));
        Assert.assertEquals(null, solutionWithValidContext.getUserByEmail("dog-3@mail.ru"));

        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);

        Assert.assertEquals(null, solutionWithInvalidContext.getUserByEmail("dog-1@mail.ru"));

        dataBase.reset();
    }

    @Test
    public void deleteUserTest(){
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.deleteUser(0);
        PowerMockito.verifyStatic(Mockito.never());
        User.deleteUser(Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.deleteUser(0);
        solutionWithValidContext.deleteUser(1);
        PowerMockito.verifyStatic(Mockito.times(2));
        User.deleteUser(Mockito.any(Integer.class));
    }
}
