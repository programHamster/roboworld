package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.model.bean.operator.BroadcastEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TrackerInitiator implements ApplicationContextAware {
    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        appContext = applicationContext;
    }

    public void control(BroadcastEvent broadcast){
        appContext.publishEvent(broadcast);
    }

}
