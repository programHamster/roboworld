package org.jazzteam.roboworld.model.facroty.taskFactory;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;

/**
 * This class describes a factory producing general tasks.
 */
public class GeneralTaskFactory extends TaskImplementationFactory {

    /**
     * Returns an array representing the nodes of available implementations.
     *
     * @return an array representing the nodes of available implementations.
     */
    protected ImplementationNode[] getImplementationNodes(){
        ImplementationNode backImplementation = new ImplementationNode
                (Constants.DIE_TASK_VALUE, DieTask.class);
        return new ImplementationNode[]{ backImplementation };
    }

}
