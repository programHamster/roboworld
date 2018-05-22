package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.model.bean.operator.BroadcastEvent;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.springframework.context.ApplicationListener;

/**
 * Tracker is used to monitor of robots of a certain type
 */
public interface Tracker extends ApplicationListener<BroadcastEvent> {
}
