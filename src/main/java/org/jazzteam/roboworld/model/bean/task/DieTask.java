package org.jazzteam.roboworld.model.bean.task;

public class DieTask extends AbstractTask {

    public void perform(){
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

}
