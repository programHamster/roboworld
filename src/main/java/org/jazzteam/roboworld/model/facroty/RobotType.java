package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.model.bean.robot.GeneralRobot;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.FrontEndDeveloperRobot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.HRRobot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;

/**
 * This enum describes the available implementations of robots. Each implementation of robot must
 * define classes (superclasses) of tasks with which it can work.
 */
public enum RobotType {
    BACK_END_DEVELOPER{
        public Class<?>[] getFeasibleTasks(){
            return new Class<?>[]{BackEndTask.class};
        }
        public Robot getRobot(){
            return new BackEndDeveloperRobot();
        }
    }, FRONT_END_DEVELOPER{
        public Class<?>[] getFeasibleTasks(){
            return new Class<?>[]{FrontEndTask.class};
        }
        public Robot getRobot(){
            return new FrontEndDeveloperRobot();
        }
    }, HR{
        public Class<?>[] getFeasibleTasks(){
            return new Class<?>[]{HRTask.class};
        }
        public Robot getRobot(){
            return new HRRobot();
        }
    }, GENERAL {
        public Class<?>[] getFeasibleTasks(){
            return new Class<?>[]{};
        }
        public Robot getRobot(){
            return new GeneralRobot();
        }
    };

    /**
     * Returns an array of classes (superclasses) that the robot can perform.
     *
     * @return an array of classes (superclasses) that the robot can perform
     */
    public abstract Class<?>[] getFeasibleTasks();

    /**
     * Returns a particular robot implementation.
     *
     * @return a particular robot implementation
     */
    public abstract Robot getRobot();

    /**
     * Returns the type of robot capable of performing the specified task.
     *
     * @param task task
     * @return the type of robot capable of performing the specified task
     */
    public static RobotType identifyRobotType(Task task){
        if(task == null){
            throw new TaskIsNullException();
        }
        RobotType result = RobotType.GENERAL;
        for(RobotType type : RobotType.values()){
            for(Class<?> taskClass : type.getFeasibleTasks()){
                if(taskClass.isInstance(task)){
                    result = type;
                    break;
                }
            }
        }
        return result;
    }
}
