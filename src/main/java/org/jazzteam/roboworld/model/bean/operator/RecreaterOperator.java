package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.exception.*;
import org.jazzteam.roboworld.output.OutputWriter;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.UUID;

public class RecreaterOperator extends AbstractOperator {
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
        return createRobot(type, name, false);
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

    public void broadcastTask(Task task, RobotType type){
        SharedBoard.getBoard(type).add(task);
        getRobots().forEach((name, robot) -> {
            try{
                robot.wakeUp();
            } catch(RobotDeadException e){
                if(recreate){
                    createRobot(robot.getRobotType(), robot.getName(), true);
                }
            }
        });
    }

    public void broadcastTask(Task task){
        broadcastTask(task, RobotType.GENERAL);
    }

    public void assignTask(Task task, Robot robot){
        if(robot == null){
            throw new NullPointerException(Constants.ROBOT_IS_NULL);
        }
        assignTask(task, robot.getName());
    }

    public void assignTask(Task task, String nameOfRobot){
        if(task == null){
            throw new TaskIsNullException();
        }
        Robot robot = get(nameOfRobot);
        if(robot == null){
            throw new RobotNotFoundException(nameOfRobot);
        }
        if(!recreate && robot.isDie()){
            remove(robot.getName());
            throw new RobotDeadException(robot);
        }
        tryAssignTask(task, robot);
    }

    private void tryAssignTask(Task task, Robot robot){
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
        } catch(RobotDeadException e){
            tryAssignTask(task, robot);
        }
    }

    private static String getRandomName(){
        final int LENGTH_NAME = 5;
        String uuid = UUID.randomUUID().toString().replaceAll(org.jazzteam.roboworld.
                Constants.DEFAULT_UUID_DELIMITER, org.jazzteam.roboworld.Constants.EMPTY);
        return uuid.substring(0, LENGTH_NAME);
    }

}
