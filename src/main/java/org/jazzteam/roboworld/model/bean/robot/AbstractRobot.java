package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.TaskBoard;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotActuationException;
import org.jazzteam.roboworld.model.exception.RobotDeadException;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractRobot implements Robot {
    private TaskBoard tasks = new TaskBoard();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    // lifecycle of robot
    public final void run(){
        try {
            activation();
            while(!isDead()){
                work();
            }
        } finally {
            shutdown();
        }
    }

    /**
     * When overriding this method, the first line should be called super.activation()
     * because robot need to grab a monitor
     */
    protected void activation(){
        if(lock.isLocked() || !lock.tryLock()){
            throw new RobotActuationException("the robot is already activated");
        }
    }

    protected abstract void work();

    /**
     * When overriding this method, it is best to do with calling super.shutdown()
     */
    protected void shutdown(){
        tasks = null;
    }

    public final void addTask(Task task){
        if(tasks != null && !isDead()){
            tasks.addTask(task);
        } else {
            throw new RobotDeadException("the robot is already dead");
        }
    }

    protected final Task getTask(){
        if(isDead()){
            throw new RobotDeadException();
        }
        return tasks.getTask();
    }

    public final void wakeUp(){
        if(lock.tryLock()){
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    protected final void await(){
        try {
            condition.await();
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            throw new RobotDeadException(e.getMessage(), e);
        }
    }

    protected static boolean isDead(){
        return Thread.currentThread().isInterrupted();
    }

}
