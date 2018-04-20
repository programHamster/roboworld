package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.notSpecified.TaskTypeNotSpecifiedException;
import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.DieTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedTaskException;

public abstract class TaskFactory {

    public static Task getTaskFromFactory(String taskType, String name) throws UnsupportedTaskException{
        if(taskType == null || taskType.isEmpty()){
            throw new TaskTypeNotSpecifiedException();
        }
        switch(taskType){
            case Constants.BACK_END_TASK_VALUE:
                return checkName(name) ? new BackEndTask() : new BackEndTask(name);
            case Constants.FRONT_END_TASK_VALUE:
                return checkName(name) ? new FrontEndTask() : new FrontEndTask(name);
            case Constants.HR_TASK_VALUE:
                return checkName(name) ? new HRTask() : new HRTask(name);
            case Constants.DIE_TASK_VALUE:
                return checkName(name) ? new DieTask() : new DieTask(name);
            default:
                throw new UnsupportedTaskException();
        }
    }

    private static boolean checkName(String name){
        return name == null || name.trim().isEmpty();
    }

}
