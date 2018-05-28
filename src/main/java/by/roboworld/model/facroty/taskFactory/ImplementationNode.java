package by.roboworld.model.facroty.taskFactory;

import by.roboworld.exception.Constants;
import by.roboworld.model.bean.task.Task;

import java.util.Objects;

/**
 * This class is used to associate the implementation of the class with the
 * name by which it will be searched. This class is compared by the
 * implementation name for a quick search of the implementation class.
 */
public class ImplementationNode implements Comparable<ImplementationNode> {
    /** The name accorded to the implementation. */
    private final String implementationName;
    /** The implementation class. */
    private final Class<? extends Task> taskClass;

    /**
     * This constructor initialises implementation and its name.
     *
     * @param implementationName implementation name
     * @param taskClass implementation class
     * @throws NullPointerException if implementation name or class is
     *                              <code>null</code>
     */
    public ImplementationNode(String implementationName, Class<? extends Task> taskClass) {
        Objects.requireNonNull(implementationName, Constants.IMPLEMENTATION_NAME_IS_NULL);
        Objects.requireNonNull(taskClass, Constants.IMPLEMENTATION_CLASS_IS_NULL);
        this.implementationName = implementationName;
        this.taskClass = taskClass;
    }

    /**
     * Returns a implementation name.
     *
     * @return a implementation name
     */
    public String getImplementationName() {
        return implementationName;
    }

    /**
     * Returns a implementation class.
     *
     * @return a implementation class
     */
    public Class<? extends Task> getTaskClass() {
        return taskClass;
    }

    /**
     * Returns a result of the comparison of names of implementations. This
     * class is compared by the implementation name for a quick search of the
     * implementation class.
     *
     * @param node other implementation node
     * @return a result of the comparison of names of implementations
     */
    public int compareTo(ImplementationNode node) {
        return implementationName.compareTo(node.implementationName);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof ImplementationNode)) {
            return false;
        }
        ImplementationNode node = (ImplementationNode) o;
        return taskClass == node.taskClass && implementationName.equals(node.implementationName);
    }
}
