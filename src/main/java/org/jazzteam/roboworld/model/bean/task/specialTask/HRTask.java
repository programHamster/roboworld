package org.jazzteam.roboworld.model.bean.task.specialTask;

public class HRTask extends AbstractSpecialTask {

    public HRTask(){}
    public HRTask(String name){
        super(name);
    }

    public void perform(){
        System.out.println("The HR task \"" + getName() + "\" is performing...");
        super.perform();
    }

}