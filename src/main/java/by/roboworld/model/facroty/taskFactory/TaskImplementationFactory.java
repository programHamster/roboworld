package by.roboworld.model.facroty.taskFactory;

import by.roboworld.exception.Constants;
import by.roboworld.model.bean.task.Task;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class describes any factory producing tasks of a certain type. This
 * separation is necessary because a robot of a particular type can perform
 * several task implementations, so necessary specify these implementations
 * in the factory implementation.
 */
public abstract class TaskImplementationFactory {

    /**
     * Returns an array representing the nodes of available implementations.
     *
     * @return an array representing the nodes of available implementations.
     */
    protected abstract ImplementationNode[] getImplementationNodes();

    /**
     * Returns a implementation class corresponding to the specified name of
     * the implementation.
     *
     * The search with logarithmic complexity is applied because despite the
     * example of tasks for each type of robot there can be a large number and
     * the search through the cycle is not advisable.
     *
     * @param implementationName the implementation name
     * @return a implementation class corresponding to the specified name of
     *         the implementation
     * @throws NullPointerException if the specified implementation name is
     *                              null or empty
     */
    public Class<?> searchImplementation(String implementationName) {
        Objects.requireNonNull(implementationName, Constants.TASK_IMPLEMENTATION_IS_NULL);
        if (implementationName.trim().isEmpty()) {
            throw new NullPointerException(Constants.TASK_IMPLEMENTATION_IS_EMPTY);
        }
        ImplementationNode[] implementations = getImplementationNodes();
        Arrays.sort(implementations);
        ImplementationNode pacifier = new ImplementationNode(implementationName, Task.class);
        int implementationIndex = Arrays.binarySearch(implementations, pacifier);
        Class<? extends Task> taskClass = null;
        if (implementationIndex >= 0) {
            taskClass = implementations[implementationIndex].getTaskClass();
        }
        return taskClass;
    }

}
