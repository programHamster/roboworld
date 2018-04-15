package org.jazzteam.roboworld.model.bean.task.generalTask;

import org.jazzteam.roboworld.model.bean.task.AbstractTask;

public class TalkerTask extends AbstractTask implements GeneralTask {
    private String message;

    public TalkerTask(String message) {
        this. message = message;
    }

    public TalkerTask(String name, String message) {
        super(name);
        this. message = message;
    }

    public void perform(){
        System.out.println(message);
    }

}
