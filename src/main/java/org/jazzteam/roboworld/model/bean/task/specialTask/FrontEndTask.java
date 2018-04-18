package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.output.OutputWriter;

public class FrontEndTask extends AbstractSpecialTask {

    public FrontEndTask(){}
    public FrontEndTask(String name){
        super(name);
    }

    public void perform(){
        OutputWriter.write("The front-end task \"" + getName() + "\" is performing...");
        super.perform();
    }

}