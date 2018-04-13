package org.jazzteam.roboworld.model.bean;

import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.TaskIsNullException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskBoard {
    // chose LinkedBlockingQueue because it has two locks.
    private final static BlockingQueue<Task> commonTasks = new LinkedBlockingQueue<>();
    private final BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();

    public static void addCommonTask(Task task){
        checkTask(task);
        commonTasks.add(task);
    }

    public final void addTask(Task task){
        checkTask(task);
        tasks.add(task);
    }

    public static Task getCommonTask(){
        return commonTasks.poll();
    }

    public final Task getTask(){
        return tasks.poll();
    }

    private static void checkTask(Task task){
        if(task == null){
            throw new TaskIsNullException();
        }
    }
}
