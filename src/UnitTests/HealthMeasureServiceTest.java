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
import ucoach.data.model.*;
import ucoach.data.ws.Authorization;
import ucoach.data.ws.CoachService;
import ucoach.data.ws.GoalService;
import ucoach.data.ws.HealthMeasureService;

import javax.xml.ws.WebServiceContext;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by nikolai on 5/7/17.
 */

@RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@PrepareForTest({Authorization.class, HealthMeasure.class, HMType.class})

public class HealthMeasureServiceTest {

    private HealthMeasureService solutionWithInvalidContext = new HealthMeasureService();
    private HealthMeasureService solutionWithValidContext = new HealthMeasureService();

    @Mock
    private WebServiceContext validContext;

    @Mock
    private WebServiceContext invalidContext;

    @Mock
    private HealthMeasure healthMeasure;

    @Mock
    List<HealthMeasure> healthMeasureList;

    @Mock
    Date date;

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(solutionWithValidContext, "context", validContext);
        Whitebox.setInternalState(solutionWithInvalidContext, "context", invalidContext);

        PowerMockito.mockStatic(HMType.class);
        PowerMockito.mockStatic(Authorization.class);
        PowerMockito.mockStatic(HealthMeasure.class);
    }

    @Test
    public void getAllHMTypesTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.getAllHMTypes();
        PowerMockito.verifyStatic(Mockito.never());
        HMType.getAll();
        Assert.assertEquals(null, solutionWithValidContext.getAllHMTypes());

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.getAllHMTypes();
        solutionWithValidContext.getAllHMTypes();
        PowerMockito.verifyStatic(Mockito.times(2));
        HMType.getAll();
    }

    @Test
    public void createHealthMeasureTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.createHealthMeasure(null, 1, 1);
        PowerMockito.verifyStatic(Mockito.never());
        HealthMeasure.createHealthMeasure(Mockito.any(HealthMeasure.class), Mockito.any(Integer.class), Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(HealthMeasure.createHealthMeasure(healthMeasure, 0, 0)).thenReturn(healthMeasure);
        Assert.assertEquals(healthMeasure, solutionWithValidContext.createHealthMeasure(healthMeasure, 0, 0));
        PowerMockito.verifyStatic(Mockito.times(1));
        HealthMeasure.createHealthMeasure(Mockito.any(HealthMeasure.class), Mockito.any(Integer.class), Mockito.any(Integer.class));
    }

    @Test
    public void getHealthMeasuresFromUserByType() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.getHealthMeasuresFromUserByType(0, 0);
        Assert.assertEquals(null, solutionWithValidContext.getHealthMeasuresFromUserByType(0, 0));
        PowerMockito.verifyStatic(Mockito.never());
        HealthMeasure.getHealthMeasuresFromUserByHMType(Mockito.any(Integer.class), Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Mockito.when(HealthMeasure.getHealthMeasuresFromUserByHMType(0, 0)).thenReturn(healthMeasureList);

        Assert.assertEquals(healthMeasureList, solutionWithValidContext.getHealthMeasuresFromUserByType(0, 0));
        PowerMockito.verifyStatic(Mockito.times(1));
        HealthMeasure.getHealthMeasuresFromUserByHMType(Mockito.any(Integer.class), Mockito.any(Integer.class));
    }

    @Test
    public void getHealthMeasuresFromUserByTypeAfterDate() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.getHealthMeasuresFromUserByTypeAfterDate(0, 0, "1993-1-1");
        PowerMockito.verifyStatic(Mockito.never());
        HealthMeasure.getHealthMeasuresFromUserByHMTypeAfterDate(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.any(Date.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Assert.assertEquals(null, solutionWithValidContext.getHealthMeasuresFromUserByTypeAfterDate(0, 0, "invalide string"));

        solutionWithValidContext.getHealthMeasuresFromUserByTypeAfterDate(0, 0, "1993-26-10");
        solutionWithValidContext.getHealthMeasuresFromUserByTypeAfterDate(0, 0, "1992-09-07");
        PowerMockito.verifyStatic(Mockito.times(2));
        HealthMeasure.getHealthMeasuresFromUserByHMTypeAfterDate(Mockito.any(Integer.class), Mockito.any(Integer.class), Mockito.any(Date.class));
    }

    @Test
    public void getHealthMeasuresFromUserByTypeAfterDateTest() {
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.getHealthMeasuresFromUserByTypeBetweenDates(0, 0, "1993-1-1", "1994-1-1");
        PowerMockito.verifyStatic(Mockito.never());
        HealthMeasure.getHealthMeasuresFromUserByHMTypeBetweenDates(Mockito.any(Integer.class), Mockito.any(Integer.class)
        , Mockito.any(Date.class), Mockito.any(Date.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        Assert.assertEquals(null, solutionWithValidContext.getHealthMeasuresFromUserByTypeBetweenDates(0, 0, "invalide string", "1992-1-1"));

        solutionWithValidContext.getHealthMeasuresFromUserByTypeBetweenDates(0, 0, "1993-26-10", "1994-1-1-");
        solutionWithValidContext.getHealthMeasuresFromUserByTypeBetweenDates(0, 0, "1992-09-07", "1998-1-1");
        PowerMockito.verifyStatic(Mockito.times(2));
        HealthMeasure.getHealthMeasuresFromUserByHMTypeBetweenDates(Mockito.any(Integer.class), Mockito.any(Integer.class)
                , Mockito.any(Date.class), Mockito.any(Date.class));

    }

    @Test
    public void deleteHealthMeasureTest(){
        Mockito.when(Authorization.validateRequest(invalidContext)).thenReturn(false);
        solutionWithInvalidContext.deleteHealthMeasure(0);
        PowerMockito.verifyStatic(Mockito.never());
        HealthMeasure.deleteHealthMeasure(Mockito.any(Integer.class));

        Mockito.when(Authorization.validateRequest(validContext)).thenReturn(true);
        solutionWithValidContext.deleteHealthMeasure(0);
        solutionWithValidContext.deleteHealthMeasure(1);
        PowerMockito.verifyStatic(Mockito.times(2));
        HealthMeasure.deleteHealthMeasure(Mockito.any(Integer.class));
    }

}


