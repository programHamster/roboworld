package org.jazzteam.roboworld.controllers;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.RobotDeadException;
import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.exception.TaskNotFeasibleException;
import org.jazzteam.roboworld.exception.TaskNotFoundException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedRobotTypeException;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedTaskException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.model.facroty.RobotType;
import org.jazzteam.roboworld.model.facroty.RobotTypeFactory;
import org.jazzteam.roboworld.model.facroty.taskFactory.TaskFactory;
import org.jazzteam.roboworld.output.OutputInformation;
import org.jazzteam.roboworld.output.RoboWorldEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * This controller is designed to provide information about robots and tasks, as well as executing commands
 * sent by the user.
 */
@RestController
@RequestMapping(Constants.MAIN_URL)
public class MainController {
    /** the operator who will carry out the management of robots */
    @Autowired
    private Operator operator;

    @GetMapping(value = "/robots", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Robot> getRobots() {
        return operator.getRobots();
    }

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<RobotType, Map<String, Task>> getTasks() {
        return TaskHolder.getInstance().getAllTasks();
    }

    @PostMapping(value = "/robots")
    public void createRobot(@RequestParam(Constants.PARAM_NAME_ROBOT_TYPE) String robotTypeName,
                            @RequestParam(Constants.PARAM_NAME_ROBOT_NAME) String robotName)
            throws UnsupportedRobotTypeException {
        RobotType robotType = RobotTypeFactory.getRobotTypeFromFactory(robotTypeName);
        Robot robot = operator.createRobot(robotType, decodeURIComponent(robotName));
        OutputInformation.write("Robot \"" + robot.getName() + "\" created", RoboWorldEvent.ROBOT);
    }

    @PostMapping(value = "/tasks")
    public void createTask(@RequestParam(Constants.PARAM_NAME_TASK_TYPE) String taskImplementation,
                            @RequestParam(Constants.PARAM_NAME_TASK_NAME) String taskName)
            throws UnsupportedTaskException {
        taskName = decodeURIComponent(taskName);
        Task task = TaskFactory.getTaskFromFactory(taskImplementation, taskName);
        TaskHolder.getInstance().putTask(task);
        OutputInformation.write("Task \"" + task.getName() + "\" created", RoboWorldEvent.TASK);
    }

    @PostMapping(value = "/assign")
    public void assignTask(@RequestParam(Constants.PARAM_NAME_TASK_NAME) String taskName,
                           @RequestParam(Constants.PARAM_NAME_ROBOT_NAME) String robotName) {
        robotName = decodeURIComponent(robotName);
        taskName = decodeURIComponent(taskName);
        Robot robot = operator.get(robotName);
        Task task = TaskHolder.getInstance().getTask(taskName, robot.getRobotType());
        if(task == null){
            task = TaskHolder.getInstance().totalSearch(taskName);
            if(task != null){
                throw new TaskNotFeasibleException(robotName, task);
            } else {
                throw new TaskNotFoundException(taskName);
            }
        }
        try {
            operator.assignTask(task, robotName);
        } catch (RobotDeadException e) {
            OutputInformation.write(e.getMessage(), RoboWorldEvent.ROBOT);
        }
    }

    @PostMapping(value = "/broadcast")
    public void broadcastTask(@RequestParam(Constants.PARAM_NAME_TASK_NAME) String taskName,
                              @RequestParam(Constants.PARAM_NAME_ROBOT_TYPE) String robotTypeName)
            throws UnsupportedRobotTypeException {
        try {
            RobotType robotType = RobotTypeFactory.getRobotTypeFromFactory(robotTypeName);
            taskName = decodeURIComponent(taskName);
            Task task = TaskHolder.getInstance().getTask(taskName, robotType);
            operator.broadcastTask(task);
        } catch (TaskIsNullException e) {
            throw new TaskNotFoundException(taskName);
        }
    }

    @ExceptionHandler({UnsupportedException.class, RuntimeException.class})
    public ResponseEntity<String> getMessageFromException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Decodes the passed UTF-8 String using an algorithm that's compatible with
     * JavaScript's <code>decodeURIComponent</code> function. Returns
     * <code>null</code> if the String is <code>null</code>.
     *
     * @param str The UTF-8 encoded String to be decoded
     * @return the decoded String
     */
    private static String decodeURIComponent(String str){
        if (str == null) {
            return null;
        }
        String result;
        try {
            result = URLDecoder.decode(str, "UTF-8");
        }
        // This exception should never occur.
        catch (UnsupportedEncodingException e) {
            result = str;
        }
        return result;
    }

}
