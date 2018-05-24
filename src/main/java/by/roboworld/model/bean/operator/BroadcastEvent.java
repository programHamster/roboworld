package by.roboworld.model.bean.operator;

import by.roboworld.exception.Constants;
import by.roboworld.model.facroty.RobotType;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * This class describes the broadcast event for trackers.
 */
public class BroadcastEvent extends ApplicationEvent {
    /** the type of robot for which the broadcast occurred */
    private RobotType type;

    /**
     * Constructor initializes fields.
     *
     * @param operator an operator performing the broadcasting
     * @param type type of robots for which broadcasting is performed
     */
    public BroadcastEvent(Operator operator, RobotType type){
        super(operator);
        Objects.requireNonNull(type, Constants.ROBOT_TYPE_IS_NULL);
        this.type = type;
    }

    /**
     * Returns type of robot.
     *
     * @return type of robot
     */
    public RobotType getType() {
        return type;
    }

    /**
     * Returns operator performing the broadcasting.
     *
     * @return operator performing the broadcasting
     */
    @Override
    public Operator getSource(){
        return (Operator)super.getSource();
    }
}
