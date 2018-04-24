package org.jazzteam.roboworld.model.facroty.taskFactory;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedTaskException;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class TaskImplementationFactoryTest {

    @DataPoints
    public static Object[][] factoryParameterResult = new Object[][]{
            {new BackEndTaskFactory(), Constants.BACK_END_TASK_VALUE, BackEndTask.class},
            {new FrontEndTaskFactory(), Constants.FRONT_END_TASK_VALUE, FrontEndTask.class},
            {new HRTaskFactory(), Constants.HR_TASK_VALUE, HRTask.class},
            {new GeneralTaskFactory(), Constants.DIE_TASK_VALUE, DieTask.class},
    };

    @Theory
    public void searchImplementation(Object... factories) {
        // arrange
        TaskImplementationFactory factory = (TaskImplementationFactory)factories[0];
        // act
        Class<?> taskClass = factory.searchImplementation((String)factories[1]);
        // assert
        assertEquals(taskClass, factories[2]);
    }

    @Test(expected = NullPointerException.class)
    public void searchImplementation_null() {
        // assert
        new BackEndTaskFactory().searchImplementation(null);
    }

}