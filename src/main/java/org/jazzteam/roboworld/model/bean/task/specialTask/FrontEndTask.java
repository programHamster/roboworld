package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.output.OutputInformation;

/**
 * This class describes the front-end task
 */
public class FrontEndTask extends AbstractSpecialTask {

    public FrontEndTask(){}
    public FrontEndTask(String name){
        super(name);
    }

    public void perform(){
        OutputInformation.write("The front-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}