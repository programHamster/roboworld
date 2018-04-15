package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.robot.SpecialRobot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.exception.RobotAlreadyExistException;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.exception.RobotNotFoundException;
import org.jazzteam.roboworld.model.facroty.RobotType;

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

    public Robot createRobot(RobotType type) throws RobotAlreadyExistException{
        return createRobot(type, null, false);
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
                        recreateRobot(robot);
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
                    robot = recreateRobot(robot);
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

    private Robot recreateRobot(Robot robot) throws RobotAlreadyExistException{
        Robot newRobot;
        if(robot instanceof SpecialRobot){
            newRobot = createRobot(((SpecialRobot) robot).getRobotType(), robot.getName(), true);
        } else {
            newRobot = createRobot(RobotType.GENERAL, robot.getName(), true);
        }
        return newRobot;
    }

}
