package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.command.Command;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedCommandException;

/**
 * This factory is used to determine the required command.
 */
public abstract class CommandFactory {

    /**
     * Returns an enumeration instance with the required implementation.
     *
     * @param commandName command name
     * @return an enumeration instance with the required implementation
     * @throws UnsupportedCommandException if the specified command is wrong
     * @throws NullPointerException if the specified command is <code>null</code>
     */
    public static Command getCommandFromFactory(String commandName) throws UnsupportedCommandException{
        if(commandName == null){
            throw new NullPointerException(org.jazzteam.roboworld.exception.Constants.COMMAND_IS_NULL);
        }
        switch(commandName){
            case Constants.COMMAND_CREATE_ROBOT:
                return Command.CREATE_ROBOT;
            case Constants.COMMAND_CREATE_TASK:
                return Command.CREATE_TASK;
            case Constants.COMMAND_GIVE_TASK:
                return Command.GIVE_TASK;
            default:
                throw new UnsupportedCommandException();
        }
    }
}
