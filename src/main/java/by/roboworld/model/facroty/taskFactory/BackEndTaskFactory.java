package by.roboworld.model.facroty.taskFactory;

import by.roboworld.Constants;
import by.roboworld.model.bean.task.specialTask.BackEndTask;

/**
 * This class describes a factory producing back-end tasks.
 */
public class BackEndTaskFactory extends TaskImplementationFactory {

    /**
     * Returns an array representing the nodes of available implementations.
     *
     * @return an array representing the nodes of available implementations.
     */
    protected ImplementationNode[] getImplementationNodes() {
        ImplementationNode backImplementation = new ImplementationNode
                (Constants.BACK_END_TASK_VALUE, BackEndTask.class);
        return new ImplementationNode[] { backImplementation };
    }

}
