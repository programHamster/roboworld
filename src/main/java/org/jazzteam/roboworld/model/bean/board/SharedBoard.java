package org.jazzteam.roboworld.model.bean.board;

import org.jazzteam.roboworld.exception.Constants;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class is used to store scheduled task boards for each type of the robot.
 * Tasks caught here will be performed by robots at the first opportunity.
 */
public class SharedBoard{
    private static volatile SharedBoard instance;
    private final Map<RobotType, Board<Task>> boards;

    private SharedBoard(){
        boards = new EnumMap<>(RobotType.class);
        for(RobotType type : RobotType.values()){
            boards.put(type, new TaskBoard<Task>());
        }
    }

    /**
     * Returns a single instance of this class.
     *
     * @return a single instance of this class.
     */
    public static SharedBoard getInstance() {
        if (instance == null) {
            synchronized(SharedBoard.class){
                if(instance == null){
                    instance = new SharedBoard();
                }
            }
        }
        return instance;
    }

    /**
     * Inserts a task into the appropriate board.
     *
     * @param task the task to add
     * @return <tt>true</tt> if this board changed as a result of the call
     * @throws TaskIsNullException if the specified task is null
     */
    public boolean put(Task task){
        if(task == null){
            throw new TaskIsNullException();
        }
        RobotType type = RobotType.identifyRobotType(task);
        return boards.get(type).add(task);
    }

    /**
     * Returns and removes the first task of the board for the specified type of the robot,
     * or returns {@code null} if board hasn't a task for the robot of the specified type.
     * If board hasn't a task for the specified type of the robot, it is searched in the general board
     * because an any robot can perform them.
     *
     * @return the first task of the board for specified type of robot, or {@code null} if this board is empty
     */
    public Task poll(RobotType type){
        checkRobotType(type);
        Task task = boards.get(type).poll();
        if(task == null){
            task = boards.get(RobotType.GENERAL).poll();
        }
        return task;
    }

    /**
     * Retrieves, but does not remove, the first task of the board for the specified type of the
     * robot, or returns {@code null} if board hasn't a task for the robot of the specified type.
     * If board hasn't a task for the specified type of the robot, it is searched in the general board
     * because an any robot can perform them.
     *
     * @return the first task of this board, or {@code null} if this board is empty
     */
    public Task get(RobotType type){
        checkRobotType(type);
        Task task = boards.get(type).get();
        if(task == null){
            task = boards.get(RobotType.GENERAL).get();
        }
        return task;
    }

    /**
     * Returns the number of tasks for the specified type of the robot.
     *
     * @param type type of the robot
     * @return the number of tasks for the specified type of the robot
     */
    public int numberTasks(RobotType type){
        checkRobotType(type);
        return boards.get(type).size();
    }

    /**
     * Performs a general check of the specified type.
     *
     * @param type type of the robot
     */
    private static void checkRobotType(RobotType type){
        if(type == null){
            throw new NullPointerException(Constants.ROBOT_TYPE_IS_NULL);
        }
    }

}