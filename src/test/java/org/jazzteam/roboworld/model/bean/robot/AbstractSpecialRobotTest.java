package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractSpecialRobotTest {
    private static final SharedBoard board = SharedBoard.getInstance();

    @Test
    public void takeSharedTask() {
        // arrange
        AbstractRobot robot = new GeneralRobot();
        Task task = new DieTask();
        callTakeSharedTaskOnlyWhenBoardIsEmpty(robot, task);
        // assert
        assertEquals(task, robot.getTask());
    }

    private void callTakeSharedTaskOnlyWhenBoardIsEmpty(AbstractRobot robot, Task task){
        boolean completed = false;
        synchronized (board){
            if(board.numberTasks(RobotType.GENERAL) == 0){
                board.put(task);
                completed = robot.takeSharedTask();
            }
        }
        if(!completed){
            callTakeSharedTaskOnlyWhenBoardIsEmpty(robot, task);
        }
    }
}