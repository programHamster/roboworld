package by.roboworld.model.bean.robot;

import by.roboworld.model.facroty.RobotType;

/**
 * This robot is not able to perform any special actions.
 */
public class GeneralRobot extends AbstractRobot {

    public RobotType getRobotType(){
        return RobotType.GENERAL;
    }
}
