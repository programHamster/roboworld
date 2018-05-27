package by.roboworld.model.bean.task.specialTask;

import by.roboworld.output.OutputInformation;

/**
 * This class describes the HR task.
 */
public class HRTask extends AbstractSpecialTask {

    public HRTask() {}

    public HRTask(String name) {
        super(name);
    }

    public void perform() {
        OutputInformation.write("The HR task \"" + getName() +
                "\" is performing...");
        super.perform();
    }

}