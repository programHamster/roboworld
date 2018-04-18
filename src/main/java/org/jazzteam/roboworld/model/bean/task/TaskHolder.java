package org.jazzteam.roboworld.model.bean.task;

import org.jazzteam.roboworld.model.bean.task.generalTask.GeneralTaskIdentifier;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.jazzteam.roboworld.model.exception.TaskIsNullException;
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
            throw new TaskIsNullException("Task is null");
        }
        String taskName = task.getName();
        if(task instanceof BackEndTask){
            allTasks.get(RobotType.BACK_END_DEVELOPER).put(taskName, task);
        } else if(task instanceof FrontEndTask){
            allTasks.get(RobotType.FRONT_END_DEVELOPER).put(taskName, task);
        } else if(task instanceof HRTask){
            allTasks.get(RobotType.HR).put(taskName, task);
        } else if(GeneralTaskIdentifier.isGeneralTask(task)){
            allTasks.get(RobotType.GENERAL).put(taskName, task);
        } else {
            throw new IllegalArgumentException("unknown task type");
        }
    }

    public Task getTask(String taskName, RobotType type){
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
