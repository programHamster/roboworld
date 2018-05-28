package by.roboworld.model.bean.robot;

import by.roboworld.exception.Constants;
import by.roboworld.exception.RobotActuationException;
import by.roboworld.exception.RobotDeadException;
import by.roboworld.exception.TaskNotFeasibleException;
import by.roboworld.model.bean.board.Board;
import by.roboworld.model.bean.board.SharedBoard;
import by.roboworld.model.bean.board.TaskBoard;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.facroty.RobotType;
import by.roboworld.output.OutputInformation;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The robot is the thread, but it does not inherit the thread, but
 * encapsulates it, so that it is impossible to override the method run() in
 * subclasses and thereby disrupt the life cycle of the robot. The robot uses
 * the lock to enable the standby mode when no outstanding tasks for it.
 */
public abstract class AbstractRobot implements Robot {
    /** This is private task board. */
    private Board<Task> tasks = new TaskBoard<>();
    /** The encapsulated thread of execution. */
    private final Thread thread;
    /** The lock for the standby mode. */
    private final ReentrantLock lock = new ReentrantLock();
    /** The condition for the standby mode. */
    private final Condition condition = lock.newCondition();
    /**
     * This flag indicates that the robot is activated and is in working
     * condition.
     */
    private boolean isRunning = false;
    /**
     *  This flag indicates that the robot is alive and can be started or it is
     *  already working.
     */
    private boolean isAlive = true;

    /**
     * This is standard constructor initialise the thread of execution.
     */
    public AbstractRobot() {
        thread = new Thread(this::run);
    }

    /**
     * This method describes a robot's life cycle. The lifecycle of the robot
     * is divided on activation, running, shutdown and dead stages.
     */
    private void run() {
        try {
            // activation stage
            activation();
            // running stage
            isRunning = true;
            while (!isDie()) {
                work();
            }
        } finally {
            // shutdown stage
            shutdown();
            /* it is repeat outside of the method shutdown() to protect it from
             overriding */
            isRunning = false;
            isAlive = false;
            // dead stage
        }
    }

    /**
     * This method is used to initialize the robot before starting work. When
     * overriding this method, the first line should be called
     * {@code super.activation()} because robot need to grab a monitor for
     * enable the standby mode.
     *
     * @throws RobotActuationException if the robot is already activated
     */
    protected void activation() {
        if (lock.isLocked() || !lock.tryLock()) {
            throw new RobotActuationException(Constants.ROBOT_IS_ALREADY_ACTIVATED);
        }
    }

    /**
     * Takes the task from the private task board and begins to perform. If the
     * private task board is empty, it takes the task from the shared board and
     * puts it in the private. If all tasks are completed, it switches to the
     * standby mode.
     *
     * @throws RobotDeadException if call the method after shutdown the robot
     */
    protected void work() {
        Task task = pollTask();
        if (task != null) {
            task.perform();
            if (!thread.isInterrupted()) {
                OutputInformation.write("The robot \"" + getName()
                        + "\" completed the task \"" + task.getName() + "\"");
            }
        } else {
            if (!takeSharedTask()) {
                await();
            }
        }
    }

    /**
     * This method takes a task from a shared task board and puts it in the
     * private. This is a part of the default behavior. It takes a task from
     * a general task board because any robot can perform them. To avoid
     * breaking this behavior, you must call {@code super.takeSharedTask()}
     * when you override it.
     *
     * @return <code>true</code> if some task is found, <code>false</code>
     *         otherwise
     * @throws RobotDeadException if call the method after shutdown the robot
     *                            and a task was found
     */
    protected boolean takeSharedTask() {
        boolean result = false;
        Task task = SharedBoard.getInstance().poll(RobotType.GENERAL);
        if (task != null) {
            result = addTask(task);
        }
        return result;
    }

    /**
     * Use this method to free resources and to terminate the work of the robot.
     * After calling this method, the robot will not be able to perform any
     * work and is considered dead. When overriding this method, you must call
     * {@code super.shutdown()}.
     */
    protected void shutdown() {
        tasks = null;
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
        isRunning = false;
        isAlive = false;
    }

    /**
     * After calling this method, the robot begins its life cycle.
     *
     * @throws RobotDeadException if the robot is already dead
     * @throws RobotActuationException if the robot was already started
     */
    public void start() {
        try {
            if (isAlive) {
                thread.start();
            } else {
                throw new RobotDeadException(this);
            }
        } catch (IllegalThreadStateException e) {
            throw new RobotActuationException(Constants.ROBOT_IS_ALREADY_ACTIVATED, e);
        }
    }

    /**
     * Changes the name of this robot.
     *
     * @param name name of the robot
     */
    public void setName(final String name) {
        thread.setName(name);
    }

    /**
     * Returns this robot's name.
     *
     * @return this robot's name.
     */
    public String getName() {
        return thread.getName();
    }

    /**
     * Adds a task to the task board of the robot. If the robot started to turn
     * off or already off, then throw the {@code RobotDeadException}. If the
     * robot determines that it will not be able to perform this task, it will
     * throw the {@code TaskNotFeasibleException}.
     *
     * @param task the task to the execute
     * @return <code>true</code> if the task was added to the board
     *         successfully and <code>false</code> otherwise
     * @throws NullPointerException if the specified task is null
     * @throws RobotDeadException if the robot started to turn off or already
     *                            off
     * @throws TaskNotFeasibleException if the robot determines that it will
     *                                  not be able to perform this task
     */
    public boolean addTask(final Task task) {
        Objects.requireNonNull(task, Constants.TASK_IS_NULL);
        checkLife();
        if (checkTaskFeasibility(task)) {
            return tasks.add(task);
        } else {
            throw new TaskNotFeasibleException(getName(), task);
        }
    }

    /**
     * Retrieves, but does not remove, the first task from the private task
     * board, or returns {@code null} if the task board is empty.
     *
     * @return the first task from the private task board, or {@code null} if
     *          the task board is empty
     * @throws RobotDeadException if the robot started to turn off or already
     *                            off
     */
    protected final Task getTask() {
        checkLife();
        return tasks.get();
    }

    /**
     * Retrieves and removes the first task from the private task board, or
     * returns {@code null} if the task board is empty.
     *
     * @return the first task from the private task board, or {@code null} if
     *          the task board is empty
     */
    protected final Task pollTask() {
        checkLife();
        return tasks.poll();
    }

    /**
     * Awakes a robot from the standby mode.
     *
     * @throws RobotDeadException if the robot started to turn off or already
     *                            off
     */
    public final void wakeUp() {
        checkLife();
        if (lock.tryLock()) {
            try {
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Activates the standby mode.
     *
     * @throws RobotDeadException if the robot started to turn off or already
     *                            off
     */
    protected final void await() {
        try {
            condition.await();
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            throw new RobotDeadException(e.getMessage(), e);
        }
    }

    /**
     * Checks whether the robot is able to perform the specified task.
     *
     * @param task the task to check
     * @return <code>true</code> if the robot can perform the specified task
     *          and <code>false</code> otherwise
     */
    protected boolean checkTaskFeasibility(final Task task) {
        RobotType taskType = RobotType.identifyRobotType(task);
        return taskType == getRobotType() || taskType == RobotType.GENERAL;
    }

    /**
     * Returns <code>true</code> if the robot was ordered to die, but he did
     * not have time to do it. This corresponds to the shutdown and dead
     * stages.
     *
     * @return <code>true</code> if the robot was to kill himself;
     *          <code>false</code> otherwise.
     */
    public final boolean isDie() {
        return thread.isInterrupted() || !isAlive;
    }

    /**
     * Throws the {@code RobotDeadException} if the robot is dead.
     *
     * @throws RobotDeadException if the robot is dead.
     */
    private void checkLife() {
        if (isDie() || tasks == null) {
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
    public final boolean isAlive() {
        return isAlive;
    }

    /**
     * Return <code>true</code> if the robot has activated and not dead.
     * This corresponds to the running stage.
     *
     * @return <code>true</code> if the robot has activated and not dead;
     *          <code>false</code> otherwise.
     */
    public final boolean isRunning() {
        return isRunning;
    }

    /**
     * Robots are considered to be equal if their threads are equal.
     *
     * @param o the other robot
     * @return <code>true</code> if robots are equal and <code>false</code>
     *                           otherwise
     */
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof AbstractRobot)) {
            return false;
        }
        AbstractRobot robot = (AbstractRobot) o;
        return thread.equals(robot.thread);
    }

    /**
     * Standard hash code generation.
     *
     * @return the hash code
     */
    public int hashCode() {
        return Objects.hash(thread, thread.getName());
    }

}
