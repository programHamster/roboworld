package org.jazzteam.roboworld;

public abstract class Constants {
    //                  Output option
    public static final String INIT_PARAM_NAME_OUTPUT = "output";
    public static final String INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT = "webSocket";
    public static final String INIT_PARAM_VALUE_SYSTEM = "system";
    //                  Operator option
    public static final String INIT_PARAM_NAME_OPERATOR = "operator";
    public static final String INIT_PARAM_VALUE_OPERATOR_RECREATOR = "recreator";
    public static final String INIT_PARAM_NAME_OPERATOR_ADDITION_PARAM = "operator addition param";
    //                  Tracker option
    public static final String INIT_PARAM_NAME_TRACKER = "tracker";
    public static final String INIT_PARAM_VALUE_TRACKER_PERFORMANCE = "performance";
    public static final String INIT_PARAM_NAME_TRACKER_ADDITION_PARAM = "tracker addition param";
    public static final String INIT_PARAM_VALUE_TRACKER_PERIOD = "5000";
    //                   Controllers
    public static final String MESSAGE_URL = "/chat";
    public static final String MAIN_URL = "/main";
    public static final int ERROR_STATUS_CODE = 500;
    public static final String HEADER_KEY_JSON = "Content-Type";
    public static final String HEADER_VALUE_JSON = "application/json";
    public static final String PARAM_NAME_INIT = "need";
    public static final String PARAM_VALUE_TASKS_INIT = "tasks";
    public static final String PARAM_VALUE_ROBOTS_INIT = "robots";
    public static final String EMPTY = "";
    public static final String DEFAULT_UUID_DELIMITER = "-";
    //                    Commands
    public static final String PARAM_NAME_COMMAND = "command";
    public static final String COMMAND_CREATE_ROBOT = "createRobot";
    public static final String COMMAND_CREATE_TASK = "createTask";
    public static final String COMMAND_GIVE_TASK = "giveTask";
    //                    Task type
    public static final String TASK = "task";
    public static final String PARAM_NAME_TASK_TYPE = "taskType";
    public static final String BACK_END_TASK_VALUE = "back";
    public static final String FRONT_END_TASK_VALUE = "front";
    public static final String HR_TASK_VALUE = "hr";
    public static final String DIE_TASK_VALUE = "die";
    //                    Give task
    public static final String PARAM_NAME_TASK_NAME = "taskName";
    public static final String PARAM_NAME_CHECKBOX = "particularRobot";
    public static final String PARAM_VALUE_CHECKBOX = "true";
    //                   Robot type
    public static final String PARAM_NAME_ROBOT_TYPE = "robotType";
    public static final String BACK_END_ROBOT_VALUE = "back";
    public static final String FRONT_END_ROBOT_VALUE = "front";
    public static final String HR_ROBOT_VALUE = "hr";
    public static final String GENERAL_ROBOT_VALUE = "general";
    public static final String PARAM_NAME_ROBOT_NAME = "robotName";
    //                   factories
    public static final String PARAM_DELIMITER = ",";
    //                   output
    public static final String KEY_DELIMITER = ":";
}
