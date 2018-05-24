package by.roboworld.controllers;

import by.roboworld.Constants;
import by.roboworld.exception.RobotDeadException;
import by.roboworld.exception.TaskNotFeasibleException;
import by.roboworld.exception.TaskNotFoundException;
import by.roboworld.exception.unsupported.UnsupportedException;
import by.roboworld.exception.unsupported.UnsupportedRobotTypeException;
import by.roboworld.exception.unsupported.UnsupportedTaskException;
import by.roboworld.model.bean.operator.Operator;
import by.roboworld.model.bean.robot.Robot;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.TaskHolder;
import by.roboworld.model.facroty.RobotType;
import by.roboworld.model.facroty.RobotTypeFactory;
import by.roboworld.model.facroty.taskFactory.TaskFactory;
import by.roboworld.output.OutputInformation;
import by.roboworld.output.RoboWorldEvent;
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

    /**
     * Returns robots belonging to the operator.
     *
     * @return robots belonging to the operator
     */
    @GetMapping(value = "/robots", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Robot> getRobots() {
        return operator.getRobots();
    }

    /**
     * Returns all previously created tasks.
     *
     * @return all previously created tasks
     */
    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<RobotType, Map<String, Task>> getTasks() {
        return TaskHolder.getInstance().getAllTasks();
    }

    /**
     * Tells the operator to create the robot in the specified type and name.
     *
     * @param robotTypeName name corresponding to the type of robot
     * @param robotName robot name
     * @throws UnsupportedRobotTypeException if cannot determine the type of robot
     */
    @PostMapping(value = "/robots")
    public void createRobot(@RequestParam(Constants.PARAM_NAME_ROBOT_TYPE) String robotTypeName,
                            @RequestParam(Constants.PARAM_NAME_ROBOT_NAME) String robotName)
            throws UnsupportedRobotTypeException {
        RobotType robotType = RobotTypeFactory.getRobotTypeFromFactory(robotTypeName);
        Robot robot = operator.createRobot(robotType, decodeURIComponent(robotName));
        OutputInformation.write("Robot \"" + robot.getName() + "\" created", RoboWorldEvent.ROBOT);
    }

    /**
     * Creates a task with the specified type and name.
     *
     * @param taskImplementation name of the task implementation corresponding to a particular task class
     * @param taskName name of task
     * @throws UnsupportedTaskException if cannot determine the type of task
     */
    @PostMapping(value = "/tasks")
    public void createTask(@RequestParam(Constants.PARAM_NAME_TASK_TYPE) String taskImplementation,
                            @RequestParam(Constants.PARAM_NAME_TASK_NAME) String taskName)
            throws UnsupportedTaskException {
        taskName = decodeURIComponent(taskName);
        Task task = TaskFactory.getTaskFromFactory(taskImplementation, taskName);
        TaskHolder.getInstance().putTask(task);
        OutputInformation.write("Task \"" + task.getName() + "\" created", RoboWorldEvent.TASK);
    }

    /**
     * Assigns a task with the specified name to a robot with the specified name.
     *
     * @param taskName name of task
     * @param robotName name of robot
     */
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

    /**
     * Broadcasts to all robots of the specified type that it is necessary to perform
     * the task with the specified name.
     *
     * @param taskName name of task
     * @param robotTypeName name corresponding to the type of robot
     * @throws UnsupportedRobotTypeException if cannot determine the type of robot
     */
    @PostMapping(value = "/broadcast")
    public void broadcastTask(@RequestParam(Constants.PARAM_NAME_TASK_NAME) String taskName,
                              @RequestParam(Constants.PARAM_NAME_ROBOT_TYPE) String robotTypeName)
            throws UnsupportedRobotTypeException {
        RobotType robotType = RobotTypeFactory.getRobotTypeFromFactory(robotTypeName);
        taskName = decodeURIComponent(taskName);
        Task task = TaskHolder.getInstance().getTask(taskName, robotType);
        if(task == null){
            throw new TaskNotFoundException(taskName);
        }
        operator.broadcastTask(task);
    }

    /**
     * Returns a response with a message if an error occurs.
     *
     * @param ex the exception is transmitted automatically by means of the Spring MVC
     * @return response with a message if an error occurs
     */
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
