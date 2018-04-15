package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.model.bean.board.Board;
import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.board.TaskBoard;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.GeneralTask;
import org.jazzteam.roboworld.model.exception.RobotActuationException;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GeneralRobot implements Robot {
    // reference to the shared task board
    private static final Board<GeneralTask> generalTaskBoard = SharedBoard.getBoard(RobotType.GENERAL);
    private Board<Task> tasks = new TaskBoard<>();
    private final Thread thread;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isRunning = false;
    private boolean isAlive = true;

    public GeneralRobot(){
        thread = new Thread(this::run);
    }

    // lifecycle of robot is divided on activation, running, shutdown, dead stages
    private void run(){
        try {
            // activation stage
            activation();
            // running stage
            isRunning = true;
            while(!isDie()){
                work();
            }
        }catch(RobotDeadException e){
            // to report the use of a dead robot
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            isRunning = false;
            // shutdown stage
            shutdown();
            isAlive = false;
            // dead stage
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

    /**
     * This is the default behavior of the robot. Returns the completed task if any task was performed.
     *
     * @return the completed task if any task was performed, <code>null</code> otherwise
     */
    protected Task work() throws RobotDeadException{
        Task task = removeTask();
        if(task != null){
            task.perform();
        } else {
            if(!takeSharedTask()){
                await();
            }
        }
        return task;
    }

    /**
     * This is a part of the default behavior. Returns <code>true</code> if some task is found
     *
     * @return <code>true</code> if some task is found, <code>false</code> otherwise
     */
    protected boolean takeSharedTask() throws RobotDeadException{
        boolean result = false;
        Task task = generalTaskBoard.poll();
        if(task != null){
            addTask(task);
            result = true;
        }
        return result;
    }

    /**
     * When overriding this method, it is best to do with calling super.shutdown()
     */
    protected void shutdown(){
        tasks = null;
    }

    public void start(){
        thread.start();
    }

    public void setName(String name){
        thread.setName(name);
    }

    public String getName(){
        return thread.getName();
    }

    public void addTask(Task task) throws RobotDeadException{
        checkLife();
        tasks.add(task);
    }

    protected final Task getTask() throws RobotDeadException{
        checkLife();
        return tasks.get();
    }

    protected final Task removeTask() throws RobotDeadException{
        checkLife();
        return tasks.poll();
    }

    public final void wakeUp() throws RobotDeadException{
        checkLife();
        if(lock.tryLock()){
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    protected final void await() throws RobotDeadException{
        try {
            condition.await();
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            throw new RobotDeadException(e.getMessage(), e);
        }
    }

    /**
     * Returns <code>true</code> if the robot was ordered to die, but he did not have time to do it.
     * This corresponds to the shutdown and dead stages.
     *
     * @return <code>true</code> if the robot was to kill himself;
     *          <code>false</code> otherwise.
     */
    public final boolean isDie(){
        return thread.isInterrupted() || !isAlive;
    }

    private void checkLife() throws RobotDeadException{
        if(isDie()){
            throw new RobotDeadException();
        }
    }

    /**
     * Return <code>true</code> if the robot has created and not dead.
     * This corresponds to the activation and running stages.
     *
     * @return <code>true</code> if the robot has created and not dead;
     *          <code>false</code> otherwise.
     */
    public final boolean isAlive(){
        return isAlive;
    }

    /**
     * Return <code>true</code> if the robot has started and not dead.
     * This corresponds to the running stage.
     *
     * @return <code>true</code> if the robot has started and not dead;
     *          <code>false</code> otherwise.
     */
    public final boolean isRunning(){
        return isRunning;
    }

}
