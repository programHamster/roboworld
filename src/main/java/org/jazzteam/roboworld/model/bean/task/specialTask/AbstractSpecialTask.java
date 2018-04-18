package org.jazzteam.roboworld.model.bean.task.specialTask;

import org.jazzteam.roboworld.model.bean.task.AbstractTask;
import org.jazzteam.roboworld.exception.TaskNotFeasibleException;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSpecialTask extends AbstractTask implements SpecialTask {
    private final int MIN_PERFORMING_TIME = 500;
    private final int MAX_PERFORMING_TIME = 2000;

    public AbstractSpecialTask(){}
    public AbstractSpecialTask(String name){
        super(name);
    }

    public void perform(){
        int performingTime = ThreadLocalRandom.current().nextInt(MIN_PERFORMING_TIME, MAX_PERFORMING_TIME);
        try {
            Thread.sleep(performingTime);
        } catch (InterruptedException e) {
            String robotName = Thread.currentThread().getName();
            throw new TaskNotFeasibleException(robotName, this);
        }
    }

}
