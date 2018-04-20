package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.command.Command;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedCommandException;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class CommandFactoryTest {

    @DataPoints
    public static Object[][] commandNames = new Object[][]{
            {Constants.COMMAND_CREATE_ROBOT, Command.CREATE_ROBOT},
            {Constants.COMMAND_CREATE_TASK, Command.CREATE_TASK},
            {Constants.COMMAND_GIVE_TASK, Command.GIVE_TASK}
    };

    @Theory
    public void getCommandFromFactory(Object... commands) throws UnsupportedCommandException {
        // act
        Command receivedCommand = CommandFactory.getCommandFromFactory((String)commands[0]);
        // assert
        assertEquals(receivedCommand, commands[1]);
    }

    @Test(expected = UnsupportedCommandException.class)
    public void getCommandFromFactory_wrongParameter() throws UnsupportedCommandException {
        // assert
        CommandFactory.getCommandFromFactory(org.jazzteam.roboworld.model.facroty.Constants.WRONG_PARAMETER);
    }

    @Test(expected = NullPointerException.class)
    public void getCommandFromFactory_null() throws UnsupportedCommandException {
        // assert
        CommandFactory.getCommandFromFactory(null);
    }
}