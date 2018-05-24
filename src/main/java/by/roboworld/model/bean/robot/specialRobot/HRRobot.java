package by.roboworld.model.bean.robot.specialRobot;

import by.roboworld.model.bean.robot.AbstractSpecialRobot;
import by.roboworld.model.facroty.RobotType;

/**
 * The robot is trained to perform HR tasks
 */
public class HRRobot extends AbstractSpecialRobot {

    public RobotType getRobotType(){
        return RobotType.HR;
    }

}
