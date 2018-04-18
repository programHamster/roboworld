package org.jazzteam.roboworld.model.bean.task.generalTask;

import org.jazzteam.roboworld.model.bean.task.Task;

public abstract class GeneralTaskIdentifier {

    /**
     * The GeneralTask is a task that implements the interface <code>GeneralTask</code> in the first place.
     * Had to resort to reflection because interface the SpecialTask inherited from interface the GeneralTask and
     * their separation would be illogical.
     *
     * @param task checked task
     * @return <code>true</code> if the task is general, <code>false</code> otherwise
     */
    public static boolean isGeneralTask(Task task){
        Class<?>[] interfaces = getInterfaces(task.getClass());
        boolean result = false;
        for(Class<?> someInterface : interfaces){
            if(someInterface == GeneralTask.class){
                result = true;
                break;
            }
        }
        return result;
    }

    public static Class<?>[] getInterfaces(Class<?> someClass){
        Class<?>[] interfaces = someClass.getInterfaces();
        if(interfaces.length == 0){
            interfaces = getInterfaces(someClass.getSuperclass());
        }
        return interfaces;
    }

}
