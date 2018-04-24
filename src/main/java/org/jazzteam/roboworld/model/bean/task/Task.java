package org.jazzteam.roboworld.model.bean.task;

/**
 * This interface represents a task that can be performed by the robots
 */
public interface Task {

    /**
     * Returns the unique ID of the task.
     *
     * @return the unique ID of the task
     */
    int getId();

    /**
     * Describes the actions to be performed.
     */
    void perform();

    /**
     * Returns the name of the task.
     *
     * @return task name
     */
    String getName();

    /**
     * Sets the task to the specified name.
     *
     * @param name a new name for the task
     */
    void setName(String name);
}
