package org.jazzteam.roboworld.model.bean.task.generalTask;

import org.jazzteam.roboworld.model.bean.task.AbstractTask;

public class DieTask extends AbstractTask implements GeneralTask {

    public DieTask(){}
    public DieTask(String name){
        super(name);
    }

    public void perform(){
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

}
