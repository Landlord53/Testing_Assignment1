import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.Result;
import UnitTests.CoachServiceTest;

/**
 * Created by nikolai on 5/7/17.
 */
public class main {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CoachServiceTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
