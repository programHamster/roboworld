package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.exception.notSpecified.RobotNameNotSpecifiedException;
import org.jazzteam.roboworld.model.bean.robot.GeneralRobot;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AbstractOperatorTest {

    @Test
    public void countRobots() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // act
        for(RobotType type : RobotType.values()){
            operator.createRobot(type);
            int numberRobots = operator.numberRobots(type);
            // assert
            assertEquals(numberRobots, 1);
        }
    }

    @Test(expected = RobotNameNotSpecifiedException.class)
    public void get_null() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // assert
        operator.get(null);
    }

    @Test(expected = NullPointerException.class)
    public void put_null() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // assert
        operator.put(null);
    }

    @Test
    public void putAndPutIfAbsent_returnPreviouslyRobot() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        final String ROBOT_NAME = "robot";
        Robot robot = new GeneralRobot();
        robot.setName(ROBOT_NAME);
        // assert
        assertNull(operator.put(robot));
        assertEquals(robot, operator.put(robot));
        assertEquals(robot, operator.putIfAbsent(robot));
    }

}