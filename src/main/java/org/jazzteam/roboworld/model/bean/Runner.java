package org.jazzteam.roboworld.model.bean;

import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.operator.OperatorRecreater;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.jazzteam.roboworld.model.bean.task.generalTask.TalkerTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.jazzteam.roboworld.model.exception.RobotAlreadyExistException;
import org.jazzteam.roboworld.model.exception.RobotDeadException;
import org.jazzteam.roboworld.model.exception.RobotNotFoundException;
import org.jazzteam.roboworld.model.facroty.RobotType;

public class Runner {
    public static void main(String[] args) {
        Operator operator = new OperatorRecreater();
        Robot beRobot = null;
        Robot feRobot = null;
        Robot hrRobot = null;
        try {
            beRobot = operator.createRobot(RobotType.BACK_END_DEVELOPER, "BERobot");
            feRobot = operator.createRobot(RobotType.FRONT_END_DEVELOPER, "FERobot");
            hrRobot = operator.createRobot(RobotType.HR, "HRRobot");
        } catch (RobotAlreadyExistException e) {
            e.printStackTrace();
        }
        Task hi = new TalkerTask("Hi");
        Task die = new DieTask("die task");
        Task be = new BackEndTask("back-End");
        Task be2 = new BackEndTask("back-End 2");
        Task fe = new FrontEndTask("front-end");
        Task fe2 = new FrontEndTask("front-end 2");
        Task hr = new HRTask("hr");
        Task hr2 = new HRTask("hr 2");

        try {
            operator.broadcastTask(hr, RobotType.HR);
            operator.broadcastTask(fe, RobotType.FRONT_END_DEVELOPER);
            operator.broadcastTask(hi);
            operator.assignTask(be, beRobot);
            operator.assignTask(fe2, feRobot);
            operator.assignTask(hr2, hrRobot);
            operator.assignTask(die, beRobot);
            Thread.sleep(10000);
            operator.assignTask(be, beRobot);
            operator.broadcastTask(be2, RobotType.BACK_END_DEVELOPER);
        } catch (RobotNotFoundException | RobotDeadException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
