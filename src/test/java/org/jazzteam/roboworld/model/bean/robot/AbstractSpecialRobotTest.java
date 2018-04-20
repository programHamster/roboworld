package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractSpecialRobotTest {
    private static final SharedBoard board = SharedBoard.getInstance();

    @Test
    public void takeSharedTask() {
        // arrange
        AbstractRobot robot = new BackEndDeveloperRobot();
        Task generalTask = new DieTask();
        synchronized (board){
            board.put(generalTask);
            assertTrue(robot.takeSharedTask());
        }
        // assert
        assertEquals(generalTask, robot.getTask());
    }
}