package by.roboworld.model.bean.tracker;

import by.roboworld.exception.Constants;
import by.roboworld.model.bean.operator.BroadcastEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

/**
 * This class serves to notify trackers about broadcast events.
 */
public class TrackerInitiator implements ApplicationContextAware {
    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext)
            throws BeansException {
        appContext = applicationContext;
    }

    /**
     * Notifies trackers about the event.
     *
     * @param broadcast a broadcast event
     */
    public void control(final BroadcastEvent broadcast) {
        Objects.requireNonNull(appContext, Constants.APPLICATION_CONTEXT_IS_NULL);
        Objects.requireNonNull(broadcast, Constants.BROADCAST_EVENT_IS_NULL);
        appContext.publishEvent(broadcast);
    }

}
