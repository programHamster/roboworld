package org.jazzteam.roboworld.model.bean.task;

public abstract class AbstractTask implements Task {
    private static int commonId;
    private final int ID;
    private String name;

    public AbstractTask() {
        ID = commonId++;
        name = "task" + ID;
    }

    public AbstractTask(String name){
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId(){
        return ID;
    }
}
