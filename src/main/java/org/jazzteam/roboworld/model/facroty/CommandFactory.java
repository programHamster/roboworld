package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.command.Command;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedCommandException;

public abstract class CommandFactory {
    public static Command getCommandFromFactory(String commandName) throws UnsupportedCommandException{
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
