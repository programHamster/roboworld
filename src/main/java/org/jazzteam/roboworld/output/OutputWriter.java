package org.jazzteam.roboworld.output;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.websocket.ChatEndpoint;
import org.jazzteam.roboworld.websocket.ChatEndpointConfigurator;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;

public abstract class OutputWriter {
    // default value
    private static String outputName = Constants.INIT_PARAM_VALUE_SYSTEM;
    private static ChatEndpoint chat;

    public static void installOutput(String outputName) throws InstantiationException{
        if(outputName == null){
            throw new NullPointerException(org.jazzteam.roboworld.exception.Constants.OUTPUT_NAME_IS_NULL);
        }
        OutputWriter.outputName = outputName;
        if(outputName.equals(Constants.INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT)){
            chat = new ChatEndpointConfigurator().getEndpointInstance(ChatEndpoint.class);
        }
    }

    public static void write(String message){
        switch (outputName){
            case Constants.INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT:
                if(chat != null){
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

    public static void write(String message, RoboWorldEvent event){
        if(event == null){
            throw new NullPointerException(org.jazzteam.roboworld.exception.Constants.EVENT_IS_NULL);
        }
        String newMessage = event.name().toLowerCase() + Constants.KEY_DELIMITER + message;
        write(newMessage);
    }

}
