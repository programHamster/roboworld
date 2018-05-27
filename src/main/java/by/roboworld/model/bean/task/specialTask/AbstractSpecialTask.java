package by.roboworld.model.bean.task.specialTask;

import by.roboworld.exception.TaskNotFeasibleException;
import by.roboworld.model.bean.task.AbstractTask;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The class describes common actions for all special tasks.
 */
public abstract class AbstractSpecialTask extends AbstractTask
        implements SpecialTask {

    public AbstractSpecialTask() {}

    public AbstractSpecialTask(final String name) {
        super(name);
    }

    /**
     * The method simulates the perform of a task.
     *
     * @throws TaskNotFeasibleException if the {@code InterruptedException}
     *                                  occurs
     */
    public void perform() {
        final int minPerformingTime = 500;
        final int maxPerformingTime = 2000;
        int performingTime = ThreadLocalRandom.current()
                .nextInt(minPerformingTime, maxPerformingTime);
        try {
            Thread.sleep(performingTime);
        } catch (InterruptedException e) {
            String robotName = Thread.currentThread().getName();
            throw new TaskNotFeasibleException(robotName, this);
        }
    }

}
