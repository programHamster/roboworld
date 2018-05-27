package by.roboworld.model.bean.task.specialTask;

import by.roboworld.output.OutputInformation;

/**
 * This class describes the back-end task.
 */
public class BackEndTask extends AbstractSpecialTask {

    public BackEndTask() {}

    public BackEndTask(final String name) {
        super(name);
    }

    public void perform() {
        OutputInformation.write("The back-end task \"" + getName() +
                "\" is performing...");
        super.perform();
    }

}
