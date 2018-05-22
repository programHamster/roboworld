package org.jazzteam.roboworld.spring;

import org.jazzteam.roboworld.model.bean.operator.AbstractOperator;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.operator.RecreaterOperator;
import org.jazzteam.roboworld.model.bean.tracker.MonitorPerformanceTracker;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;
import org.jazzteam.roboworld.model.bean.tracker.TrackerInitiator;
import org.jazzteam.roboworld.output.OutputInformation;
import org.jazzteam.roboworld.output.implementation.WebSocketOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RoboworldConfig {

    @Bean
    public Operator operator(){
        AbstractOperator operator = new RecreaterOperator(true);
        operator.setTrackerInitiator(trackerInitiator());
        return operator;
    }

    @Bean
    public Tracker tracker(){
        final int TRACKER_PERIOD = 5000;
        return new MonitorPerformanceTracker(TRACKER_PERIOD);
    }

    @PostConstruct
    public void initOutput(){
        OutputInformation.installOutput(new WebSocketOutput());
    }

    @Bean
    public TrackerInitiator trackerInitiator(){
        return new TrackerInitiator();
    }

}
