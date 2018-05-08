package org.jazzteam.roboworld;

import org.jazzteam.roboworld.output.OutputEnum;

public abstract class Setting {
    //                  Output option
    public static final OutputEnum output = OutputEnum.WEB_SOCKET;
    //                  Operator option
    public static final String INIT_PARAM_VALUE_OPERATOR = "recreator";
    public static final String INIT_PARAM_VALUE_OPERATOR_ADDITION_PARAM = "true";
    //                  Tracker option
    public static final String INIT_PARAM_VALUE_TRACKER = "performance";
    public static final String INIT_PARAM_VALUE_TRACKER_PERIOD = "5000";
}
