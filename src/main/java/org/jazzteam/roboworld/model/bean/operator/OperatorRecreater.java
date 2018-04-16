package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotAlreadyExistException;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.exception.RobotNotFoundException;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.UUID;

public class OperatorRecreater extends AbstractOperator {
    private boolean recreate = true;

    public OperatorRecreater(){
    }

    public OperatorRecreater(boolean recreateOfRobot){
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
            getRobots().put(robot.getName(), robot);
        } else {
            if(getRobots().putIfAbsent(robot.getName(), robot) != null){
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
                    try {
                        createRobot(robot.getRobotType(), robot.getName(), true);
                    } catch (RobotAlreadyExistException e1) {
                        // never happen
                    }
                }
            }
        });
    }

    public void broadcastTask(Task task){
        broadcastTask(task, RobotType.GENERAL);
    }

    public void assignTask(Task task, Robot robot) throws RobotNotFoundException, RobotDeadException{
        if(robot == null){
            throw new NullPointerException("Robot is null");
        }
        assignTask(task, robot.getName());
    }

    public void assignTask(Task task, String nameOfRobot) throws RobotNotFoundException, RobotDeadException{
        Robot robot = getRobots().get(nameOfRobot);
        if(robot == null){
            throw new RobotNotFoundException();
        }
        if(!recreate && robot.isDie()){
            getRobots().remove(robot.getName());
            throw new RobotDeadException(robot);
        }
        tryAssignTask(task, robot);
    }

    private void tryAssignTask(Task task, Robot robot){
        try{
            if(recreate && robot.isDie()){
                try{
                    robot = createRobot(robot.getRobotType(), robot.getName(), true);
                    System.out.println(robot.getName() + " is recreated");
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
        // max length is 36
        final int LENGTH_NAME = 5;
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String shortUuid = uuid.substring(0, LENGTH_NAME);
        return shortUuid;
    }

}
