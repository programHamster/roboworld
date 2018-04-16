package org.jazzteam.roboworld.model.bean.tracker;

import java.util.Timer;

public abstract class TimeTracker implements Tracker {
    private final Timer timer;
    private long period;

    public TimeTracker(long period){
        this.period = period;
        timer = new Timer(true);
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    protected Timer getTimer(){
        return timer;
    }

}
