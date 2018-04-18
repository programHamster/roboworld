package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.output.OutputWriter;

public class BackEndTask extends AbstractSpecialTask {

    public BackEndTask(){}
    public BackEndTask(String name){
        super(name);
    }

    public void perform(){
        OutputWriter.write("The back-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}
