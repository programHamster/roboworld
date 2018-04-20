package org.jazzteam.roboworld.model.bean.board;

import org.jazzteam.roboworld.exception.Constants;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.EnumMap;
import java.util.Map;

public class SharedBoard{
    private static volatile SharedBoard instance;
    private final Map<RobotType, Board<Task>> boards;

    private SharedBoard(){
        boards = new EnumMap<>(RobotType.class);
        for(RobotType type : RobotType.values()){
            boards.put(type, new TaskBoard<Task>());
        }
    }

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

    public boolean put(Task task){
        if(task == null){
            throw new TaskIsNullException(Constants.TASK_IS_NULL);
        }
        RobotType type = RobotType.identifyRobotType(task);
        return boards.get(type).add(task);
    }

    public Task poll(RobotType type){
        checkRobotType(type);
        Task task = boards.get(type).poll();
        if(task == null){
            task = boards.get(RobotType.GENERAL).poll();
        }
        return task;
    }

    public Task get(RobotType type){
        checkRobotType(type);
        Task task = boards.get(type).get();
        if(task == null){
            task = boards.get(RobotType.GENERAL).get();
        }
        return task;
    }

    public int numberTasks(RobotType type){
        checkRobotType(type);
        return boards.get(type).size();
    }

    private static void checkRobotType(RobotType type){
        if(type == null){
            throw new NullPointerException(Constants.ROBOT_TYPE_IS_NULL);
        }
    }

}