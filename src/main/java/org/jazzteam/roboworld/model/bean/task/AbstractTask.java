package org.jazzteam.roboworld.model.bean.task;

public abstract class AbstractTask implements Task {
    private static int commonId;
    private final int ID;

    public AbstractTask() {
        ID = commonId++;
    }

    public int getId(){
        return ID;
    }
}
