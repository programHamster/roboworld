package org.jazzteam.roboworld.model.bean.task;

public class TalkerTask extends AbstractTask {
    private String message;

    public TalkerTask(String message) {
        this. message = message;
    }

    public void perform(){
        System.out.println(message);
    }

}
