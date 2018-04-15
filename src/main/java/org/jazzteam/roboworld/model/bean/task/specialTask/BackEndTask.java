package org.jazzteam.roboworld.model.bean.task.specialTask;

public class BackEndTask extends AbstractSpecialTask {

    public BackEndTask(){}
    public BackEndTask(String name){
        super(name);
    }

    public void perform(){
        System.out.println("The back-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}
