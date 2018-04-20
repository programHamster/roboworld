package org.jazzteam.roboworld.model.bean.robot;

import org.jazzteam.roboworld.exception.*;
import org.jazzteam.roboworld.model.bean.board.Board;
import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.board.TaskBoard;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.output.OutputWriter;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractRobot implements Robot {
    private Board<Task> tasks = new TaskBoard<>();
    private final Thread thread;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isRunning = false;
    private boolean isAlive = true;

    public AbstractRobot(){
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
        } finally {
            // shutdown stage
            shutdown();
            // dead stage
        }
    }

    /**
     * When overriding this method, the first line should be called super.activation()
     * because robot need to grab a monitor
     */
    protected void activation(){
        if(lock.isLocked() || !lock.tryLock()){
            throw new RobotActuationException(Constants.ROBOT_IS_ALREADY_ACTIVATED);
        }
    }

    protected void work() {
        Task task = removeTask();
        if(task != null){
            task.perform();
            OutputWriter.write("The robot \"" + getName() + "\" completed the task \"" + task.getName() + "\"");
        } else {
            if(!takeSharedTask()){
                await();
            }
        }
    }

    /**
     * This is a part of the default behavior. Returns <code>true</code> if some task is found
     *
     * @return <code>true</code> if some task is found, <code>false</code> otherwise
     */
    protected boolean takeSharedTask() {
        boolean result = false;
        Task task = SharedBoard.getInstance().poll(RobotType.GENERAL);
        if(task != null){
            result = addTask(task);
        }
        return result;
    }

    /**
     * When overriding this method, it is best to do with calling super.shutdown()
     */
    protected void shutdown(){
        tasks = null;
        if(!thread.isInterrupted()){
            thread.interrupt();
        }
        isRunning = false;
        isAlive = false;
        OutputWriter.write("The robot \"" + getName() + "\" is dead.");
    }

    public void start(){
        try {
            if(isAlive){
                thread.start();
            } else {
                throw new RobotDeadException(this);
            }
        } catch (IllegalThreadStateException e) {
            throw new RobotActuationException(Constants.ROBOT_IS_ALREADY_ACTIVATED);
        }
    }

    public void setName(String name){
        thread.setName(name);
    }

    public String getName(){
        return thread.getName();
    }

    public boolean addTask(Task task) {
        if(task == null){
            throw new TaskIsNullException();
        }
        checkLife();
        if(checkTaskFeasibility(task)){
            return tasks.add(task);
        } else {
            throw new TaskNotFeasibleException(getName(), task);
        }
    }

    protected final Task getTask() {
        checkLife();
        return tasks.get();
    }

    protected final Task removeTask() {
        checkLife();
        return tasks.poll();
    }

    public final void wakeUp(){
        checkLife();
        if(lock.tryLock()){
            try {
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    protected final void await() {
        try {
            condition.await();
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            throw new RobotDeadException(e.getMessage(), e);
        }
    }

    protected boolean checkTaskFeasibility(Task task){
        RobotType taskType = RobotType.identifyRobotType(task);
        return taskType == getRobotType() || taskType == RobotType.GENERAL;
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

    private void checkLife() {
        if(isDie() || tasks == null){
            throw new RobotDeadException(this);
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

    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(o == this){
            return true;
        }
        if(!(o instanceof AbstractRobot)){
            return false;
        }
        AbstractRobot robot = (AbstractRobot)o;
        return robot.thread.getName().equals(thread.getName()) && thread.equals(robot.thread);
    }

    public int hashCode(){
        return Objects.hash(thread, thread.getName());
    }

}
