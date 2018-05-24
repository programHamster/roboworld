package by.roboworld.model.facroty;

import by.roboworld.model.bean.robot.GeneralRobot;
import by.roboworld.model.bean.robot.Robot;
import by.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import by.roboworld.model.bean.robot.specialRobot.FrontEndDeveloperRobot;
import by.roboworld.model.bean.robot.specialRobot.HRRobot;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.generalTask.DieTask;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;
import by.roboworld.model.bean.task.specialTask.HRTask;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class RobotTypeTest {

    @DataPoints
    public static Object[][] robotTypes = new Object[][]{
            {RobotType.BACK_END_DEVELOPER, BackEndDeveloperRobot.class},
            {RobotType.FRONT_END_DEVELOPER, FrontEndDeveloperRobot.class},
            {RobotType.HR, HRRobot.class},
            {RobotType.GENERAL, GeneralRobot.class},
    };

    @Theory
    public void getRobot(Object... types) {
        // act
        Robot robot = ((RobotType)types[0]).getRobot();
        // assert
        assertEquals(robot.getClass(), types[1]);
    }

    @Test
    public void identifyRobotType() {
        // arrange
        Task generalTask = new DieTask();
        Task backEndTask = new BackEndTask();
        Task frontEndTask = new FrontEndTask();
        Task hrTask = new HRTask();
        // act
        RobotType generalType = RobotType.identifyRobotType(generalTask);
        RobotType backEndType = RobotType.identifyRobotType(backEndTask);
        RobotType frontEndType = RobotType.identifyRobotType(frontEndTask);
        RobotType hrType = RobotType.identifyRobotType(hrTask);
        // assert
        assertEquals(RobotType.GENERAL, generalType);
        assertEquals(RobotType.BACK_END_DEVELOPER, backEndType);
        assertEquals(RobotType.FRONT_END_DEVELOPER, frontEndType);
        assertEquals(RobotType.HR, hrType);
    }

    @Test(expected = NullPointerException.class)
    public void identifyRobotType_null() {
        // assert
        RobotType.identifyRobotType(null);
    }
}