package by.roboworld.model.bean.tracker;

import by.roboworld.model.bean.operator.BroadcastEvent;
import org.springframework.context.ApplicationListener;

/**
 * Tracker is used to monitor of robots of a certain type
 */
public interface Tracker extends ApplicationListener<BroadcastEvent> {
}
