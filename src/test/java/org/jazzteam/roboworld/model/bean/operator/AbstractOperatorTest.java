package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.exception.notSpecified.RobotNameNotSpecifiedException;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractOperatorTest {

    @Test
    public void countRobots() {
        // arrange
        RecreaterOperator operator = new RecreaterOperator();
        // act
        for(RobotType type : RobotType.values()){
            operator.createRobot(type);
            int numberRobots = operator.countRobots(type);
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
}