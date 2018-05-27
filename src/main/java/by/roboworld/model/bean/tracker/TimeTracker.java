package by.roboworld.model.bean.tracker;

import by.roboworld.exception.Constants;

import java.util.Timer;

/**
 * This class is used to set the timer in the tracker and allows classes which
 * extends it to schedule the start of control.
 */
public abstract class TimeTracker implements Tracker {
    /** Timer encapsulated in this class. */
    private final Timer timer;
    /** The period for setting up control startup. */
    private long period;

    /**
     * This constructor initializes the tracker and period.
     *
     * @param period period for setting up control startup
     */
    public TimeTracker(long period) {
        setPeriod(period);
        timer = new Timer(true);
    }

    /**
     * Returns the period with which the {@code control()} will be called.
     *
     * @return the period with which the {@code control()} will be called
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Sets the period with which the {@code control()} will be called.
     *
     * @param period period for setting up control startup
     * @throws IllegalArgumentException if the specified period has a negative
     *                                  or zero value
     */
    public void setPeriod(long period) {
        if (period <= 0) {
            throw new IllegalArgumentException(Constants.PERIOD_IS_NOT_POSITIVE);
        }
        this.period = period;
    }

    /**
     * Returns a timer for classes implementing this abstract class.
     *
     * @return the timer
     */
    protected Timer getTimer() {
        return timer;
    }

}
