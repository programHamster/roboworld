package org.jazzteam.roboworld.model.bean.board;

import org.jazzteam.roboworld.model.bean.task.Task;
import org.jazzteam.roboworld.model.bean.task.generalTask.GeneralTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.BackEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.FrontEndTask;
import org.jazzteam.roboworld.model.bean.task.specialTask.HRTask;
import org.jazzteam.roboworld.model.facroty.RobotType;

public abstract class SharedBoard {
    private static final Board<BackEndTask> backEndTaskBoard = new TaskBoard<>();
    private static final Board<FrontEndTask> frontEndTaskBoard = new TaskBoard<>();
    private static final Board<HRTask> hrTaskBoard = new TaskBoard<>();
    private static final Board<GeneralTask> generalTaskBoard = new TaskBoard<>();

    @SuppressWarnings("unchecked")
    public static <T extends Task> Board<T> getBoard(RobotType type){
        switch(type){
            case BACK_END_DEVELOPER: return (Board<T>)backEndTaskBoard;
            case FRONT_END_DEVELOPER: return (Board<T>)frontEndTaskBoard;
            case HR: return (Board<T>)hrTaskBoard;
            default: return (Board<T>)generalTaskBoard;
        }
    }

}
