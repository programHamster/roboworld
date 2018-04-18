package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.model.facroty.OutputFactory;

public class FrontEndTask extends AbstractSpecialTask {

    public FrontEndTask(){}
    public FrontEndTask(String name){
        super(name);
    }

    public void perform(){
        OutputFactory.println("The front-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}