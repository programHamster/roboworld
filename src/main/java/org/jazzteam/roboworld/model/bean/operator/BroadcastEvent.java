package org.jazzteam.roboworld.model.bean.operator;

import org.jazzteam.roboworld.model.facroty.RobotType;
import org.springframework.context.ApplicationEvent;

public class BroadcastEvent extends ApplicationEvent {
    private RobotType type;

    public BroadcastEvent(Operator operator, RobotType type){
        super(operator);
        this.type = type;
    }

    public RobotType getType() {
        return type;
    }

    @Override
    public Operator getSource(){
        return (Operator)super.getSource();
    }
}
