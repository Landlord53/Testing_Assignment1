package UnitTests;

import database.DataBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import ucoach.data.model.Coach;
import ucoach.data.ws.Authorization;
import ucoach.data.ws.CoachService;

import javax.xml.ws.WebServiceContext;

/**
 * Created by nikolai on 5/7/17.
 */

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@PrepareForTest({Authorization.class, Coach.class})

public class CoachServiceTest {

    private CoachService solutionWithInvalidContext = new CoachService();
    private CoachService solutionWithValidContext = new CoachService();
    private DataBase dataBase = new DataBase();
    private Coach coach;
    private Coach coach1;

    @Mock
    private WebServiceContext validContext;

    @Mock
    private WebServiceContext invalidContext;

    @Before
    public void setUp()
    {
        //solutionWithValidContext.context = validContext;
        //solutionWithInvalidContext.context = invalidContext;
        Whitebox.setInternalState(solutionWithValidContext, "context", validContext);
        Whitebox.setInternalState(solutionWithInvalidContext, "context", invalidContext);

        PowerMockito.mockStatic(Coach.class);
        PowerMockito.mockStatic(Authorization.class);

        coach = new Coach();
        coach.setUsers(null);
        coach.setPassword("12345678");
        coach.setEmail("dog-1@mail.ru");
        coach.setFirstname("Nikolay");
        coach.setLastname("Chalykh");
        coach.setId(0);

        coach1 = new Coach();
        coach1.setUsers(null);
        coach1.setPassword("12345678");
        coach1.setEmail("dog-2@mail.ru");
        coach1.setFirstname("Dmitrii");
        coach1.setLastname("Mironov");
        coach1.setId(1);
    }

    @Test
    public void getCoachTest() {
        dataBase.createCoach(coach);
        dataBase.createCoach(coach1);

        Mockito.when(Coach.getCoachById(0)).thenReturn(dataBase.getCoachById(0));
        Mockito.when(Coach.getCoachById(1)).thenReturn(dataBase.getCoachById(1));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);

        Assert.assertEquals(coach, solutionWithValidContext.getCoach(0));
        Assert.assertEquals(coach1, solutionWithValidContext.getCoach(1));
        Assert.assertEquals(null, solutionWithValidContext.getCoach(2));

        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);

        Assert.assertEquals(null, solutionWithInvalidContext.getCoach(0));

        dataBase.reset();
    }

    @Test
    public void createCoachTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);

        Assert.assertEquals(null, solutionWithInvalidContext.createCoach(coach));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);

        Mockito.when(Coach.createCoach(coach)).thenReturn(dataBase.createCoach(coach));
        Mockito.when(Coach.createCoach(coach1)).thenReturn(dataBase.createCoach(coach1));

        Assert.assertEquals(2, dataBase.getCoachesNum());
        Assert.assertEquals(coach, solutionWithValidContext.createCoach(coach));
        Assert.assertEquals(coach1, solutionWithValidContext.createCoach(coach1));

        dataBase.reset();
    }

    @Test
    public void updateCoachTest() {
        dataBase.createCoach(coach);

        Coach newCoach = new Coach();
        newCoach.setFirstname("John");
        newCoach.setLastname("Sinna");
        newCoach.setId(1);

        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        Assert.assertEquals(null, solutionWithInvalidContext.updateCoach(newCoach));

        Coach coachMustBeReturned = new Coach();
        coachMustBeReturned.setId(1);
        coachMustBeReturned.setFirstname("John");
        coachMustBeReturned.setLastname("Sinna");
        coachMustBeReturned.setPassword("12345678");
        coachMustBeReturned.setEmail("dog-2@mail.ru");

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(Coach.updateCoach(newCoach)).thenReturn(dataBase.updateCoach(coachMustBeReturned));
        Coach updatedCoach = solutionWithValidContext.updateCoach(newCoach);
        Assert.assertEquals(coachMustBeReturned, updatedCoach);

        dataBase.reset();
    }

    @Test
    public void deleteCoachTest(){
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithValidContext.deleteCoach(0);
        PowerMockito.verifyStatic(Mockito.never());
        Coach.deleteCoach(Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.deleteCoach(0);
        solutionWithValidContext.deleteCoach(1);
        PowerMockito.verifyStatic(Mockito.times(2));
        Coach.deleteCoach(Mockito.any(Integer.class));
    }
}
