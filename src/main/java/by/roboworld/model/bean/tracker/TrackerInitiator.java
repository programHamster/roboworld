package by.roboworld.model.bean.tracker;

import by.roboworld.model.bean.operator.BroadcastEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This class serves to notify trackers about broadcast events.
 */
public class TrackerInitiator implements ApplicationContextAware {
    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        appContext = applicationContext;
    }

    /**
     * Notifies trackers about the event.
     *
     * @param broadcast a broadcast event
     */
    public void control(BroadcastEvent broadcast) {
        appContext.publishEvent(broadcast);
    }

}
