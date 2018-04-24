package org.jazzteam.roboworld.output;

/**
 * This enumeration contains the available events to trigger data refresh.
 */
public enum RoboWorldEvent {
    /** trigger this event if the state or the number of robots has changed */
    ROBOT,
    /** trigger this event if the state or the number of tasks has changed */
    TASK
}
