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
public class TaskFactoryTest {

    @DataPoints
    public static Object[][] taskNames = new Object[][]{
            {Constants.BACK_END_TASK_VALUE, BackEndTask.class},
            {Constants.FRONT_END_TASK_VALUE, FrontEndTask.class},
            {Constants.HR_TASK_VALUE, HRTask.class},
            {Constants.DIE_TASK_VALUE, DieTask.class},
    };

    @Theory
    public void getTaskFromFactory(Object... tasks) throws UnsupportedTaskException {
        // act
        Task task = TaskFactory.getTaskFromFactory((String)tasks[0], null);
        // assert
        assertEquals(task.getClass(), tasks[1]);
    }

    @Test(expected = NullPointerException.class)
    public void getTaskFromFactory_null() throws UnsupportedTaskException {
        // assert
        TaskFactory.getTaskFromFactory(null, "");
    }

    @Test(expected = UnsupportedTaskException.class)
    public void getTaskFromFactory_wrongParameter() throws UnsupportedTaskException {
        // assert
        TaskFactory.getTaskFromFactory(org.jazzteam.roboworld.model.facroty.Constants.WRONG_PARAMETER, "");
    }

}