package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.output.OutputWriter;

public class HRTask extends AbstractSpecialTask {

    public HRTask(){}
    public HRTask(String name){
        super(name);
    }

    public void perform(){
        OutputWriter.write("The HR task \"" + getName() + "\" is performing...");
        super.perform();
    }

}