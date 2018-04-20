package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.exception.*;
import org.jazzteam.roboworld.model.bean.tracker.Tracker;
import org.jazzteam.roboworld.output.OutputWriter;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecreaterOperator extends AbstractOperator {
    private List<Tracker> trackers = new ArrayList<>();
    private boolean recreate = true;

    public RecreaterOperator(){
    }

    public RecreaterOperator(boolean recreateOfRobot){
        this.recreate = recreateOfRobot;
    }

    public boolean isRecreateRobot() {
        return recreate;
    }

    public void setRecreateRobot(boolean recreateRobot) {
        this.recreate = recreateRobot;
    }

    public Robot createRobot(RobotType type, String name) throws RobotAlreadyExistException{
        Robot robot;
        if(name == null || (name = name.trim()).isEmpty()){
            robot = createRobot(type);
        } else {
            robot = createRobot(type, name, false);
        }
        return robot;
    }

    public Robot createRobot(RobotType type) {
        Robot robot;
        try {
            robot = createRobot(type, getRandomName(), false);
        } catch (RobotAlreadyExistException e) {
            // if the names matched try again
            robot = createRobot(type);
        }
        return robot;
    }

    private Robot createRobot(RobotType type, String name, boolean withReplacement) throws RobotAlreadyExistException{
        if(type == null){
            throw new NullPointerException(Constants.ROBOT_TYPE_IS_NULL);
        }
        Robot robot = type.getRobot();
        if(name != null){
            robot.setName(name);
        }
        robot.start();
        if(withReplacement){
            put(robot);
        } else {
            if(putIfAbsent(robot) != null){
                throw new RobotAlreadyExistException(robot);
            }
        }
        return robot;
    }

    public boolean broadcastTask(Task task){
        boolean success = SharedBoard.getInstance().put(task);
        getRobots().forEach((name, robot) -> {
            try{
                robot.wakeUp();
            } catch(RobotDeadException e){
                if(recreate){
                    createRobot(robot.getRobotType(), robot.getName(), true);
                }
            }
        });
        RobotType type = RobotType.identifyRobotType(task);
        trackers.forEach(tracker -> tracker.control(type));
        return success;
    }

    public boolean assignTask(Task task, String robotName){
        if(task == null){
            throw new TaskIsNullException();
        }
        Robot robot = get(robotName);
        if(robot == null){
            throw new RobotNotFoundException(robotName);
        }
        if(!recreate && robot.isDie()){
            remove(robot.getName());
            throw new RobotDeadException(robot);
        }
        return tryAssignTask(task, robot);
    }

    private boolean tryAssignTask(Task task, Robot robot){
        boolean success = false;
        try{
            if(recreate && robot.isDie()){
                try{
                    robot = createRobot(robot.getRobotType(), robot.getName(), true);
                    OutputWriter.write("Robot \"" + robot.getName() + "\" is recreated");
                } catch(RobotAlreadyExistException e){
                    // never happen
                }
            }
            robot.addTask(task);
            robot.wakeUp();
            success = true;
        } catch(RobotDeadException e){
            tryAssignTask(task, robot);
        }
        return success;
    }

    public boolean addTracker(Tracker tracker){
        if(tracker == null){
            throw new NullPointerException(Constants.TRACKER_IS_NULL);
        }
        return trackers.add(tracker);
    }

    private static String getRandomName(){
        final int LENGTH_NAME = 5;
        String uuid = UUID.randomUUID().toString().replaceAll(org.jazzteam.roboworld.
                Constants.DEFAULT_UUID_DELIMITER, org.jazzteam.roboworld.Constants.EMPTY);
        return uuid.substring(0, LENGTH_NAME);
    }

}
