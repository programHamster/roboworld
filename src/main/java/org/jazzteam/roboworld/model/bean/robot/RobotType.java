package org.jazzteam.roboworld.model.bean.robot;

public enum RobotType {
    DOCILE{
        public Robot getRobot(){
            return new DocileRobot();
        }
    };

    public abstract Robot getRobot();
}
