package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.model.facroty.RobotType;

/**
 * Tracker is used to monitor of robots of a certain type
 */
public interface Tracker {

    /**
     * Makes control of robots of the specified type.
     *
     * @param type robot type
     */
    void control(RobotType type);
}
