package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedRobotTypeException;
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
            {Constants.BACK_END_ROBOT_VALUE, RobotType.BACK_END_DEVELOPER},
            {Constants.FRONT_END_ROBOT_VALUE, RobotType.FRONT_END_DEVELOPER},
            {Constants.HR_ROBOT_VALUE, RobotType.HR},
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
        RobotType type = RobotTypeFactory.getRobotTypeFromFactory
                (org.jazzteam.roboworld.model.facroty.Constants.WRONG_PARAMETER);
    }

    @Test(expected = RobotTypeNotSpecifiedException.class)
    public void getRobotTypeFromFactory_null() throws UnsupportedRobotTypeException {
        // assert
        RobotType type = RobotTypeFactory.getRobotTypeFromFactory(null);
    }

}