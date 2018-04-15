package org.jazzteam.roboworld.model.bean.task;

public interface Task {
    int getId();
    void perform();
    String getName();
    void setName(String name);
}
