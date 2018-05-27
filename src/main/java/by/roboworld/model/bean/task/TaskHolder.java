package by.roboworld.model.bean.task;

import by.roboworld.exception.Constants;
import by.roboworld.exception.notSpecified.RobotTypeNotSpecifiedException;
import by.roboworld.exception.notSpecified.TaskNameNotSpecifiedException;
import by.roboworld.model.facroty.RobotType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class is used to store all previously created tasks, so that you do not
 * have to create a new one after the task is completed. The class implements
 * singleton pattern.
 */
public final class TaskHolder {
    private static volatile TaskHolder instance;
    /** The map stores tasks and divides them by type. */
    private final Map<RobotType, Map<String, Task>> allTasks;

    /**
     * This constructor initializes the map with a thread-safe implementation
     * {@code ConcurrentHashMap} for each type in the {@code EnumMap}.
     */
    private TaskHolder() {
        allTasks = new EnumMap<>(RobotType.class);
        for (RobotType type : RobotType.values()) {
            allTasks.put(type, new ConcurrentHashMap<>());
        }
    }

    /**
     * Returns the single instance of this class.
     *
     * @return the single instance of this class
     */
    public static TaskHolder getInstance() {
        if (instance == null) {
            synchronized (TaskHolder.class) {
                if (instance == null) {
                    instance = new TaskHolder();
                }
            }
        }
        return instance;
    }

    /**
     * Puts the task in the map intended for robots of this type.
     *
     * @param task task to save
     * @throws NullPointerException if the specified task is null
     */
    public void putTask(Task task) {
        Objects.requireNonNull(task, Constants.TASK_IS_NULL);
        String taskName = task.getName();
        RobotType type = RobotType.identifyRobotType(task);
        allTasks.get(type).put(taskName, task);
    }

    /**
     * Returns a task with the specified name for the specified robot type. If
     * the task with this name was not found among special tasks, then search
     * by general tasks, because any robot can perform them.
     *
     * @param taskName name of the desired task
     * @param type type of robot that will perform this task
     * @return a task with the specified name
     * @throws TaskNameNotSpecifiedException if the task name is null or empty
     * @throws RobotTypeNotSpecifiedException if the robot type is null
     */
    public Task getTask (String taskName, RobotType type) {
        Objects.requireNonNull(taskName, Constants.TASK_NAME_IS_NULL);
        if (taskName.isEmpty()) {
            throw new TaskNameNotSpecifiedException();
        }
        Objects.requireNonNull(type, Constants.ROBOT_TYPE_IS_NULL);
        Task task = allTasks.get(type).get(taskName);
        if (task == null) {
            task = allTasks.get(RobotType.GENERAL).get(taskName);
        }
        return task;
    }

    /**
     * Returns the task with the specified name using search for all types, if
     * the task is not found returns <code>null</code>.
     *
     * @param taskName task name
     * @return Returns the task with the specified name or <code>null</code> if
     *         the task is not found
     * @throws TaskNameNotSpecifiedException if the task name is empty
     */
    public Task totalSearch(String taskName) {
        Objects.requireNonNull(taskName, Constants.TASK_NAME_IS_NULL);
        if (taskName.isEmpty()) {
            throw new TaskNameNotSpecifiedException();
        }
        Task result = null;
        for (Map<String, Task> map : allTasks.values()) {
            Task task = map.get(taskName);
            if (task != null) {
                result = task;
                break;
            }
        }
        return result;
    }

    /**
     * Returns a map that stores all tasks. This map is unmodifiable because
     * only the {@code TaskHolder} can save new tasks.
     *
     * @return unmodifiable map that stores all tasks
     */
    public Map<RobotType, Map<String, Task>> getAllTasks() {
        Map<RobotType, Map<String, Task>> allTasksCopy = new EnumMap<>(allTasks);
        allTasksCopy.replaceAll((type, tasks) -> Collections.unmodifiableMap(tasks));
        return Collections.unmodifiableMap(allTasksCopy);
    }

}
