package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.model.facroty.OutputFactory;

public class HRTask extends AbstractSpecialTask {

    public HRTask(){}
    public HRTask(String name){
        super(name);
    }

    public void perform(){
        OutputFactory.println("The HR task \"" + getName() + "\" is performing...");
        super.perform();
    }

}