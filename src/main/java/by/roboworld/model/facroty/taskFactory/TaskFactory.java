package by.roboworld.model.facroty.taskFactory;

import by.roboworld.exception.unsupported.UnsupportedTaskException;
import by.roboworld.model.bean.task.Task;

import java.lang.reflect.InvocationTargetException;

/**
 * Factory used to produce tasks with the specified implementation.
 */
public abstract class TaskFactory {
    /** The array contains all factories producing tasks. */
    private static final TaskImplementationFactory[] TASK_IMPLEMENTATION_FACTORIES;
    static {
        TASK_IMPLEMENTATION_FACTORIES = new TaskImplementationFactory[]{
                new BackEndTaskFactory(),
                new FrontEndTaskFactory(),
                new HRTaskFactory(),
                new GeneralTaskFactory()
        };
    }

    /**
     * Returns a new task the specified implementation and with the specified
     * name. If the name does not matter, you can pass <code>null</code>.
     *
     * This factory could be implemented not through a cycle, but through a
     * switch, but then the user would have to enter an additional parameter
     * (type). I think that the cycle of a small number of types of factories
     * is a small fee for the convenience of using this factory.
     *
     * @param taskImplementation task implementation name
     * @param taskName task name
     * @return a new task the specified implementation and with the specified
     *         name if name is specified
     * @throws UnsupportedTaskException if task implementation is not found
     * @throws NullPointerException if task implementation name is null
     * @throws IllegalArgumentException if task implementation name is empty
     */
    public static Task getTaskFromFactory(String taskImplementation, String taskName)
            throws UnsupportedTaskException {
        Class<?> taskClass = null;
        for (TaskImplementationFactory factory : TASK_IMPLEMENTATION_FACTORIES) {
            Class<?> implementation = factory.searchImplementation(taskImplementation);
            if (implementation != null) {
                taskClass = implementation;
                break;
            }
        }
        if (taskClass == null) {
            throw new UnsupportedTaskException(taskImplementation);
        }
        Task task;
        try {
            if (taskName != null && !taskName.trim().isEmpty()) {
                task = (Task) taskClass.getConstructor(String.class)
                        .newInstance(taskName);
            } else {
                task = (Task) taskClass.newInstance();
            }
        } catch (IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            throw new UnsupportedTaskException(e);
        }
        return task;
    }

}
