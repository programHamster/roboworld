package by.roboworld.model.bean.operator;

import by.roboworld.model.facroty.RobotType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractOperatorTest {

    @Test
    public void numberRobots() {
        // arrange
        AbstractOperator operator = new RecreaterOperator();
        // act
        for(RobotType type : RobotType.values()){
            operator.createRobot(type);
            int numberRobots = operator.numberRobots(type);
            // assert
            assertEquals(numberRobots, 1);
        }
    }

    @Test(expected = NullPointerException.class)
    public void get_null() {
        // arrange
        AbstractOperator operator = new RecreaterOperator();
        // assert
        operator.get(null);
    }

    @Test(expected = NullPointerException.class)
    public void put_null() {
        // arrange
        AbstractOperator operator = new RecreaterOperator();
        // assert
        operator.put(null);
    }

}