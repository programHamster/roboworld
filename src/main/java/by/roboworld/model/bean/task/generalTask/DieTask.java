package by.roboworld.model.bean.task.generalTask;

import by.roboworld.model.bean.task.AbstractTask;
import by.roboworld.output.OutputInformation;

/**
 * The task for the robot to kill itself.
 */
public class DieTask extends AbstractTask implements GeneralTask {

    public DieTask() {}

    public DieTask(String name) {
        super(name);
    }

    public void perform() {
        Thread thread = Thread.currentThread();
        OutputInformation.write("The robot \"" + thread.getName() +
                "\" is dead.");
        thread.interrupt();
    }

}
