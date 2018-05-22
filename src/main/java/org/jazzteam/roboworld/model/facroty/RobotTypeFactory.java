package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedRobotTypeException;

import java.util.Objects;

/**
 * This factory is designed to determine the particular type of robot by its name.
 */
public abstract class RobotTypeFactory {

    /**
     * returns a robot type by the specified name.
     *
     * @param robotType name of the robot type
     * @return a robot type by the specified name
     * @throws UnsupportedRobotTypeException if the specified robot name is wrong
     * @throws RobotTypeNotSpecifiedException if the specified name of the robot is <code>null</code>
     */
    public static RobotType getRobotTypeFromFactory(String robotType) throws UnsupportedRobotTypeException{
        Objects.requireNonNull(robotType, "Robot type is null");
        if(robotType.isEmpty()){
            throw new RobotTypeNotSpecifiedException();
        }
        switch (robotType){
            case Constants.BACK_END_ROBOT_VALUE:
                return RobotType.BACK_END_DEVELOPER;
            case Constants.FRONT_END_ROBOT_VALUE:
                return RobotType.FRONT_END_DEVELOPER;
            case Constants.HR_ROBOT_VALUE:
                return RobotType.HR;
            case Constants.GENERAL_ROBOT_VALUE:
                return RobotType.GENERAL;
            default:
                throw new UnsupportedRobotTypeException();
        }
    }
}
