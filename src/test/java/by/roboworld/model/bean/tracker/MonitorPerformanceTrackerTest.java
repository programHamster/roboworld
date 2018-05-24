package by.roboworld.model.bean.tracker;

import by.roboworld.model.bean.board.SharedBoard;
import by.roboworld.model.bean.operator.BroadcastEvent;
import by.roboworld.model.bean.operator.Operator;
import by.roboworld.model.bean.task.Task;
import by.roboworld.model.bean.task.specialTask.BackEndTask;
import by.roboworld.model.facroty.RobotType;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MonitorPerformanceTrackerTest {

    @Test
    public void onApplicationEvent() throws InterruptedException {
        final long PERIOD = 1;
        final RobotType ROBOT_TYPE_TEST = RobotType.BACK_END_DEVELOPER;
        final Task TASK = new BackEndTask();
        // arrange
        Tracker tracker = new MonitorPerformanceTracker(PERIOD);

        Operator operator = mock(Operator.class);
        when(operator.numberRobots(ROBOT_TYPE_TEST)).thenReturn(0).thenReturn(1);
        when(operator.createRobot(ROBOT_TYPE_TEST)).thenReturn(ROBOT_TYPE_TEST.getRobot());

        BroadcastEvent event = mock(BroadcastEvent.class);
        when(event.getType()).thenReturn(ROBOT_TYPE_TEST);
        when(event.getSource()).thenReturn(operator);

        SharedBoard.getInstance().put(TASK);
        // act
        tracker.onApplicationEvent(event);
        Thread.sleep(PERIOD + 500);
        // assert
        verify(operator).createRobot(ROBOT_TYPE_TEST);
    }
}