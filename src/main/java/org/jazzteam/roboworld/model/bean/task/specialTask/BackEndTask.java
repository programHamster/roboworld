package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.output.OutputInformation;

/**
 * This class describes the back-end task
 */
public class BackEndTask extends AbstractSpecialTask {

    public BackEndTask(){}
    public BackEndTask(String name){
        super(name);
    }

    public void perform(){
        OutputInformation.write("The back-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}
