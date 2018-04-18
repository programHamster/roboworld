package org.jazzteam.roboworld.model.bean.board;

import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.exception.TaskIsNullException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskBoard<T extends Task> implements Board<T> {
    // chose LinkedBlockingQueue because it has two locks.
    private final BlockingQueue<T> tasks = new LinkedBlockingQueue<>();

    public void add(T task){
        checkTask(task);
        tasks.add(task);
    }

    public T poll(){
        return tasks.poll();
    }

    public T get(){
        return tasks.peek();
    }

    public int size(){
        return tasks.size();
    }

    private static void checkTask(Task task){
        if(task == null){
            throw new TaskIsNullException();
        }
    }
}
