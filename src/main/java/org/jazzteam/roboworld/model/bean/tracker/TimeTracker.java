package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.exception.Constants;

import java.util.Timer;

public abstract class TimeTracker implements Tracker {
    private final Timer timer;
    private long period;

    public TimeTracker(long period){
        setPeriod(period);
        timer = new Timer(true);
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        if(period <= 0){
            throw new IllegalArgumentException(Constants.PERIOD_IS_NOT_POSITIVE);
        }
        this.period = period;
    }

    protected Timer getTimer(){
        return timer;
    }

}
