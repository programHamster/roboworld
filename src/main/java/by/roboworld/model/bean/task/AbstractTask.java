package by.roboworld.model.bean.task;

import by.roboworld.Constants;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class describes common actions specific to each task.
 */
public abstract class AbstractTask implements Task {
    /** The total task count. */
    private static final AtomicInteger TOTAL_COUNT = new AtomicInteger();
    /** Task id. */
    private final int id;
    /** Task name. */
    private String name;

    /**
     * The constructor initialises the task id and name.
     */
    public AbstractTask() {
        id = TOTAL_COUNT.incrementAndGet();
        name = Constants.TASK + id;
    }

    /**
     * The constructor initialises the task with the specified name.
     *
     * @param name task name
     */
    public AbstractTask(final String name) {
        this();
        setName(name);
    }

    /**
     * Returns the name of the task.
     *
     * @return task name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the task to the specified name.
     *
     * @param name a new name for the task
     */
    public void setName(final String name) {
        Objects.requireNonNull(name, by.roboworld.exception.Constants.TASK_NAME_IS_NULL);
        this.name = name;
    }

    /**
     * Returns the unique id of the task.
     *
     * @return the unique id of the task
     */
    public int getId() {
        return id;
    }

    public String toString() {
        return "Task" + id + ";" + name;
    }

    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof AbstractTask)) {
            return false;
        }
        AbstractTask task = (AbstractTask) o;
        return task.id == id && task.name.equals(name);
    }

    public int hashCode() {
        return Objects.hash(name, id);
    }

}
