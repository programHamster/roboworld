package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.model.bean.robot.GeneralRobot;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.BackEndDeveloperRobot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.FrontEndDeveloperRobot;
import org.jazzteam.roboworld.model.bean.robot.specialRobot.HRRobot;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;

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
    protected Robot robot;

    public abstract Class<?>[] getFeasibleTasks();
    public abstract Robot getRobot();
}
