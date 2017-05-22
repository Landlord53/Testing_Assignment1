package UnitTests;

import database.DataBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.MockInjection;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import ucoach.data.model.Coach;
import ucoach.data.model.Goal;
import ucoach.data.model.User;
import ucoach.data.ws.Authorization;
import ucoach.data.ws.CoachService;
import ucoach.data.ws.GoalService;

import javax.xml.ws.WebServiceContext;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by nikolai on 5/7/17.
 */

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@PrepareForTest({Authorization.class, Goal.class})

public class GoalServiceTest {

    private GoalService solutionWithInvalidContext = new GoalService();
    private GoalService solutionWithValidContext = new GoalService();
    private Goal goal;
    private Goal goal2;
    private User user;

    @Mock
    private WebServiceContext validContext;

    @Mock
    private WebServiceContext invalidContext;

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(solutionWithValidContext, "context", validContext);
        Whitebox.setInternalState(solutionWithInvalidContext, "context", invalidContext);

        PowerMockito.mockStatic(Goal.class);
        PowerMockito.mockStatic(Authorization.class);

        goal = new Goal();
        goal.setId(0);
        goal.setUser(user);
        goal.setDueDate(new Date(1993, 10, 26));
        goal.setFrequency("Often");
        goal.setAchieved(0);

        goal2 = new Goal();
        goal2.setId(1);
        goal2.setUser(user);
        goal2.setDueDate(new Date(1992, 10, 16));
        goal2.setFrequency("Not Often");
        goal2.setAchieved(0);

        user = new User();
        user.setPassword("12345678");
        user.setEmail("dog-1@mail.ru");
        user.setFirstname("Nikolay");
        user.setLastname("Chalykh");

        List<Goal> goals = new ArrayList<>();
        goals.add(goal);
        goals.add(goal2);

        user.setGoals(goals);
        user.setId(0);
    }

    @Test
    public void createGoalTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);

        Assert.assertEquals(null, solutionWithInvalidContext.createGoal(goal, 0, 0));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);

        Mockito.when(Goal.createGoal(goal, 0,0)).thenReturn(goal);
        Mockito.when(Goal.createGoal(goal2, 0, 1)).thenReturn(goal2);
        Mockito.when(Goal.createGoal(goal, 20, 1)).thenReturn(null);

        Assert.assertEquals(goal, solutionWithValidContext.createGoal(goal, 0,0));
        Assert.assertEquals(goal2, solutionWithValidContext.createGoal(goal2, 0, 1));
        Assert.assertEquals(null, solutionWithValidContext.createGoal(goal, 20, 0));
    }

    @Test
    public void getGoalsFromUser() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        Assert.assertEquals(null, solutionWithInvalidContext.getGoalsFromUser(0));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(Goal.getGoalsFromUser(0)).thenReturn(user.getGoals());
        Mockito.when(Goal.getGoalsFromUser(20)).thenReturn(null);

        Assert.assertEquals(user.getGoals(), solutionWithValidContext.getGoalsFromUser(0));
        Assert.assertEquals(null, solutionWithValidContext.getGoalsFromUser(20));
    }

    @Test
    public void getGoalsFromUserAfterDueDateTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithValidContext.getGoalsFromUserAfterDueDate(0, "1993-1-1");
        PowerMockito.verifyStatic(Mockito.times(0));
        Goal.getGoalsFromUserAfterDueDate(Mockito.any(Integer.class), Mockito.any(Date.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.getGoalsFromUserAfterDueDate(0, "1993-1-1");
        solutionWithValidContext.getGoalsFromUserAfterDueDate(0, "1993-1-1");
        PowerMockito.verifyStatic(Mockito.times(2));
        Goal.getGoalsFromUserAfterDueDate(Mockito.any(Integer.class), Mockito.any(Date.class));

        Assert.assertEquals(null, solutionWithValidContext.getGoalsFromUserAfterDueDate(0, "invalid Date"));
    }

    @Test
    public void getGoalsFromUserByFrequencyAndDueDateTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithValidContext.getGoalsFromUserByFrequencyAndDueDate(0, "freq", "1993-1-1");
        PowerMockito.verifyStatic(Mockito.times(0));
        Goal.getGoalsFromUserAfterDueDate(Mockito.any(Integer.class), Mockito.any(Date.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.getGoalsFromUserByFrequencyAndDueDate(0, "freq", "1993-1-1");
        solutionWithValidContext.getGoalsFromUserByFrequencyAndDueDate(0, "freq", "1993-1-1");
        PowerMockito.verifyStatic(Mockito.times(2));
        Goal.getGoalsFromUserByFrequencyAndDueDate(Mockito.any(Integer.class), Mockito.any(String.class), Mockito.any(Date.class));

        Assert.assertEquals(null, solutionWithValidContext.getGoalsFromUserByFrequencyAndDueDate(0, "freq", "invalid Date"));
    }

    @Test
    public void getGoalsFromUserByTypeTest(){
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithValidContext.getGoalsFromUserByType(0, 0);
        PowerMockito.verifyStatic(Mockito.never());
        Goal.getGoalsFromUserByHMType(Mockito.any(Integer.class), Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.getGoalsFromUserByType(0, 0);
        solutionWithValidContext.getGoalsFromUserByType(1, 0);
        PowerMockito.verifyStatic(Mockito.times(2));
        Goal.getGoalsFromUserByHMType(Mockito.any(Integer.class), Mockito.any(Integer.class));
    }

    @Test
    public void getGoalsFromUserByTypeAndStatusTest(){
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithValidContext.getGoalsFromUserByTypeAndStatus(0,0, 0);
        PowerMockito.verifyStatic(Mockito.never());
        Goal.getGoalsFromUserByHMTypeAndAchievedStatus(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.getGoalsFromUserByTypeAndStatus(0, 0,0);
        solutionWithValidContext.getGoalsFromUserByTypeAndStatus(1, 0,0);
        PowerMockito.verifyStatic(Mockito.times(2));
        Goal.getGoalsFromUserByHMTypeAndAchievedStatus(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.any(Integer.class));
    }

    @Test
    public void deleteGoalTest(){
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithValidContext.deleteGoal(0);
        PowerMockito.verifyStatic(Mockito.never());
        Goal.deleteGoal(Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.deleteGoal(0);
        solutionWithValidContext.deleteGoal(1);
        PowerMockito.verifyStatic(Mockito.times(2));
        Goal.deleteGoal(Mockito.any(Integer.class));
    }

    @Test
    public void achieveGoalTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        Assert.assertEquals(null, solutionWithValidContext.achieveGoal(0));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(Goal.getGoalById(0)).thenReturn(Mockito.mock(Goal.class));

        solutionWithValidContext.achieveGoal(0);
        solutionWithValidContext.achieveGoal(1);

        PowerMockito.verifyStatic(Mockito.times(2));
        Goal.achieveGoal(Mockito.any(Goal.class));
    }
}
