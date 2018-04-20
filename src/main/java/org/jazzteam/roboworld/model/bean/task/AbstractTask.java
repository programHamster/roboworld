package org.jazzteam.roboworld.model.bean.task;

import org.jazzteam.roboworld.Constants;

import java.util.Objects;

public abstract class AbstractTask implements Task {
    private static int commonId;
    private final int ID;
    private String name;

    public AbstractTask() {
        ID = commonId++;
        name = Constants.TASK + ID;
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

    public String toString(){
        return "Task" + ID + ";" + name;
    }

    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(o == this){
            return true;
        }
        if(!(o instanceof AbstractTask)){
            return false;
        }
        AbstractTask task = (AbstractTask)o;
        return task.ID == ID && task.name.equals(name);
    }

    public int hashCode(){
        return Objects.hash(name, ID);
    }

}
