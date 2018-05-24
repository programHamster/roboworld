package by.roboworld.model.bean.task;

import by.roboworld.Constants;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class describes common actions specific to each task.
 */
public abstract class AbstractTask implements Task {
    /** the total task count */
    private static volatile AtomicInteger totalCount = new AtomicInteger();
    /** task id */
    private final int ID;
    /** task name */
    private String name;

    /**
     * The constructor initialises the task ID and name.
     */
    public AbstractTask() {
        ID = totalCount.incrementAndGet();
        name = Constants.TASK + ID;
    }

    /**
     * The constructor initialises the task with the specified name.
     *
     * @param name task name
     */
    public AbstractTask(String name){
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
    public void setName(String name) {
        Objects.requireNonNull(name, by.roboworld.exception.Constants.TASK_NAME_IS_NULL);
        this.name = name;
    }

    /**
     * Returns the unique ID of the task.
     *
     * @return the unique ID of the task
     */
    public int getId(){
        return ID;
    }

    public String toString(){
        return "Task" + ID + ";" + name;
    }

    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(o == this){
            return true;
        }
        if(!(o instanceof AbstractTask)){
            return false;
        }
        AbstractTask task = (AbstractTask)o;
        return task.ID == ID && task.name.equals(name);
    }

    public int hashCode(){
        return Objects.hash(name, ID);
    }

}
