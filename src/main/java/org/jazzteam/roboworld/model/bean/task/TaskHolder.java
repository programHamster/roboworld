package org.jazzteam.roboworld.model.bean.task;

import org.jazzteam.roboworld.exception.Constants;
import org.jazzteam.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.exception.notSpecified.TaskNameNotSpecifiedException;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskHolder {
    private static volatile TaskHolder instance;
    private final Map<RobotType, Map<String, Task>> allTasks;

    private TaskHolder(){
        allTasks = new EnumMap<>(RobotType.class);
        for(RobotType type : RobotType.values()){
            allTasks.put(type, new ConcurrentHashMap<>());
        }
    }

    public static TaskHolder getInstance() {
        if (instance == null) {
            synchronized(TaskHolder.class){
                if(instance == null){
                    instance = new TaskHolder();
                }
            }
        }
        return instance;
    }

    public void putTask(Task task){
        if(task == null){
            throw new TaskIsNullException(Constants.TASK_IS_NULL);
        }
        String taskName = task.getName();
        RobotType type = RobotType.identifyRobotType(task);
        allTasks.get(type).put(taskName, task);
    }

    public Task getTask(String taskName, RobotType type){
        if(taskName == null || taskName.isEmpty()){
            throw new TaskNameNotSpecifiedException();
        }
        if(type == null){
            throw new RobotTypeNotSpecifiedException();
        }
        Task task = allTasks.get(type).get(taskName);
        if(task == null){
            task = allTasks.get(RobotType.GENERAL).get(taskName);
        }
        return task;
    }

    public Map<RobotType, Map<String, Task>> getAllTasks(){
        Map<RobotType, Map<String, Task>> copyAllTasks = new EnumMap<>(allTasks);
        copyAllTasks.replaceAll((type, tasks) -> Collections.unmodifiableMap(tasks));
        return Collections.unmodifiableMap(copyAllTasks);
    }

}
