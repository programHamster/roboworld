package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedRobotTypeException;

public abstract class RobotTypeFactory {
    public static RobotType getRobotTypeFromFactory(String robotType) throws UnsupportedRobotTypeException{
        if(robotType == null || robotType.isEmpty()){
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
