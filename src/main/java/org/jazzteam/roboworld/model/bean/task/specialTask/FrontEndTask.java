package org.jazzteam.roboworld.model.bean.task.specialTask;

public class FrontEndTask extends AbstractSpecialTask {

    public FrontEndTask(){}
    public FrontEndTask(String name){
        super(name);
    }

    public void perform(){
        System.out.println("The front-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}