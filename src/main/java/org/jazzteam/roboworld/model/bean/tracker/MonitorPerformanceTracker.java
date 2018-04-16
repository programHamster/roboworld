package org.jazzteam.roboworld.model.bean.tracker;

import org.jazzteam.roboworld.model.bean.board.SharedBoard;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.robot.Robot;
import org.jazzteam.roboworld.model.facroty.RobotType;

import java.util.EnumMap;
import java.util.Map;
import java.util.TimerTask;

public class MonitorPerformanceTracker extends TimeTracker {
    private final Map<RobotType, MonitorPerformance> timerTasks = new EnumMap<>(RobotType.class);
    private final Operator operator;

    public MonitorPerformanceTracker(Operator operator, long period) {
        super(period);
        this.operator = operator;
    }

    public void control(RobotType type){
        getTimer().purge();
        MonitorPerformance monitor = new MonitorPerformance(type);
        getTimer().schedule(monitor, getPeriod(), getPeriod());
        changeMonitor(type, monitor);
    }

    private void changeMonitor(RobotType type, MonitorPerformance newMonitor){
        MonitorPerformance oldMonitor = timerTasks.get(type);
        if(oldMonitor != null){
            oldMonitor.cancel();
        }
        timerTasks.put(type, newMonitor);
    }

    private class MonitorPerformance extends TimerTask {
        private final RobotType type;

        private MonitorPerformance(RobotType type){
            this.type = type;
        }

        public void run(){
            int numberTasks = SharedBoard.getBoard(type).size();
            int numberRobots;
            if(type == RobotType.GENERAL){
                numberRobots = operator.countRobots();
            } else {
                numberRobots = operator.countRobots(type);
            }
            if(numberTasks > numberRobots){
                Robot robot = operator.createRobot(type);
                System.out.println(type.name() + " robots can not cope, created an additional robot " + robot.getName());
            } else {
                this.cancel();
            }
        }
    }

}
