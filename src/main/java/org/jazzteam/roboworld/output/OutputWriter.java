package org.jazzteam.roboworld.output;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.websocket.ChatEndpoint;
import org.jazzteam.roboworld.websocket.ChatEndpointConfigurator;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;

/**
 * This class is used to display information about what is happening in the game world.
 * The default way to display information is the console ({@code System.out.println()}.
 * For change the way information is displayed, you must implement it in method {@code write(String message)}
 * and associate it with a specific name.
 */
public abstract class OutputWriter {
    /** The default way to display information */
    private static String outputName = Constants.INIT_PARAM_VALUE_SYSTEM;
    /** stores of the endpoint for all subscribed users */
    private static ChatEndpoint chat;

    /**
     * Installs the way information is displayed. If web socket output is set, the endpoint reference
     * is initialized.
     *
     * @param outputName output name
     * @throws NullPointerException if the specified output name is <code>null</code>
     */
    public static void installOutput(String outputName) {
        if(outputName == null){
            throw new NullPointerException(org.jazzteam.roboworld.exception.Constants.OUTPUT_NAME_IS_NULL);
        }
        OutputWriter.outputName = outputName;
        if(outputName.equals(Constants.INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT)){
            chat = new ChatEndpointConfigurator().getEndpointInstance(ChatEndpoint.class);
        }
    }

    /**
     * Sends the message to the installed output.
     *
     * @param message message to output
     * @throws IllegalArgumentException if the output name is not associated with the output way
     */
    public static void write(String message){
        switch (outputName){
            case Constants.INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT:
                if(chat != null && !Thread.interrupted()){
                    Set<Session> sessions = chat.getUserSessions();
                    sessions.forEach(session -> {
                        try {
                            session.getBasicRemote().sendText(message);
                        } catch (IOException e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    });
                    break;
                }
            case Constants.INIT_PARAM_VALUE_SYSTEM:
                System.out.println(message);
                break;
            default:
                throw new IllegalArgumentException(org.jazzteam.roboworld.exception.Constants.UNKNOWN_OUTPUT_NAME);
        }
    }

    /**
     * Adds the specified event key to the message and sends it to the output. If the status or number of robots
     * or tasks changed, necessary specify the appropriate event to trigger data refresh on the client side.
     *
     * @param message message to output
     * @param event event occurred in the game world
     * @throws NullPointerException if the specified event is <code>null</code>
     * @throws IllegalArgumentException if the output name is not associated with the output way
     */
    public static void write(String message, RoboWorldEvent event){
        if(event == null){
            throw new NullPointerException(org.jazzteam.roboworld.exception.Constants.EVENT_IS_NULL);
        }
        String newMessage = event.name().toLowerCase() + Constants.KEY_DELIMITER + message;
        write(newMessage);
    }

}
