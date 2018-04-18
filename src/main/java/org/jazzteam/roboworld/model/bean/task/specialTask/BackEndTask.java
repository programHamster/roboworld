package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.model.facroty.OutputFactory;

public class BackEndTask extends AbstractSpecialTask {

    public BackEndTask(){}
    public BackEndTask(String name){
        super(name);
    }

    public void perform(){
        OutputFactory.println("The back-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}
