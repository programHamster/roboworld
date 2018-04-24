package org.jazzteam.roboworld.model.facroty.taskFactory;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;

/**
 * This class describes a factory producing HR tasks.
 */
public class HRTaskFactory extends TaskImplementationFactory {

    /**
     * Returns an array representing the nodes of available implementations.
     *
     * @return an array representing the nodes of available implementations.
     */
    protected ImplementationNode[] getImplementationNodes(){
        ImplementationNode backImplementation = new ImplementationNode
                (Constants.HR_TASK_VALUE, HRTask.class);
        return new ImplementationNode[]{ backImplementation };
    }

}
