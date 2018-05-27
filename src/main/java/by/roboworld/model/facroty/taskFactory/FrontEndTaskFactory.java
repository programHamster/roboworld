package by.roboworld.model.facroty.taskFactory;

import by.roboworld.Constants;
import by.roboworld.model.bean.task.specialTask.FrontEndTask;

/**
 * This class describes a factory producing front-end tasks.
 */
public class FrontEndTaskFactory extends TaskImplementationFactory {

    /**
     * Returns an array representing the nodes of available implementations.
     *
     * @return an array representing the nodes of available implementations.
     */
    protected ImplementationNode[] getImplementationNodes() {
        ImplementationNode backImplementation = new ImplementationNode
                (Constants.FRONT_END_TASK_VALUE, FrontEndTask.class);
        return new ImplementationNode[] { backImplementation };
    }

}
