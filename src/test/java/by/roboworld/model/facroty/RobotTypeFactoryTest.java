package by.roboworld.model.facroty;

import by.roboworld.Constants;
import by.roboworld.exception.unsupported.UnsupportedRobotTypeException;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class RobotTypeFactoryTest {

    @DataPoints
    public static Object[][] robotTypes = new Object[][]{
            {by.roboworld.Constants.BACK_END_ROBOT_VALUE, RobotType.BACK_END_DEVELOPER},
            {by.roboworld.Constants.FRONT_END_ROBOT_VALUE, RobotType.FRONT_END_DEVELOPER},
            {by.roboworld.Constants.HR_ROBOT_VALUE, RobotType.HR},
            {Constants.GENERAL_ROBOT_VALUE, RobotType.GENERAL},
    };

    @Theory
    public void getRobotTypeFromFactory(Object... types) throws UnsupportedRobotTypeException {
        // act
        RobotType type = RobotTypeFactory.getRobotTypeFromFactory((String)types[0]);
        // assert
        assertEquals(type, types[1]);
    }

    @Test(expected = UnsupportedRobotTypeException.class)
    public void getRobotTypeFromFactory_wrongParameter() throws UnsupportedRobotTypeException {
        // assert
        RobotTypeFactory.getRobotTypeFromFactory
                (by.roboworld.model.facroty.Constants.WRONG_PARAMETER);
    }

    @Test(expected = NullPointerException.class)
    public void getRobotTypeFromFactory_null() throws UnsupportedRobotTypeException {
        // assert
        RobotTypeFactory.getRobotTypeFromFactory(null);
    }

}