package by.roboworld.spring;

import by.roboworld.model.bean.operator.AbstractOperator;
import by.roboworld.model.bean.operator.Operator;
import by.roboworld.model.bean.operator.RecreaterOperator;
import by.roboworld.model.bean.tracker.MonitorPerformanceTracker;
import by.roboworld.model.bean.tracker.Tracker;
import by.roboworld.model.bean.tracker.TrackerInitiator;
import by.roboworld.output.OutputInformation;
import by.roboworld.output.implementation.WebSocketOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Here are the settings of the application.
 */
@Configuration
public class RoboworldConfig {

    /**
     * Returns an operator through which robots will be managed. In this method, you can specify the
     * necessary implementation of the operator and set a necessary values.
     *
     * @return an operator through which robots will be managed
     */
    @Bean
    public Operator operator(){
        AbstractOperator operator = new RecreaterOperator(true);
        operator.setTrackerInitiator(trackerInitiator());
        return operator;
    }

    /**
     * Returns a tracker, which will monitor robots. In this method, you can specify the necessary
     * implementation of the tracker and set a necessary values.
     *
     * @return a tracker, which will monitor robots
     */
    @Bean
    public Tracker tracker(){
        final int TRACKER_PERIOD = 5000;
        return new MonitorPerformanceTracker(TRACKER_PERIOD);
    }

    /**
     * In this method, you can set the output method about roboworld.
     */
    @PostConstruct
    public void initOutput(){
        OutputInformation.installOutput(new WebSocketOutput());
    }

    /**
     * Returns trackerInitiator which notifies trackers about the broadcast event.
     *
     * @return trackerInitiator which notifies trackers about the broadcast event
     */
    @Bean
    public TrackerInitiator trackerInitiator(){
        return new TrackerInitiator();
    }

}
