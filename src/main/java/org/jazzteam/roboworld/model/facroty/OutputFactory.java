package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.websocket.ChatEndpoint;
import org.jazzteam.roboworld.websocket.ChatEndpointConfigurator;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;

public abstract class OutputFactory {
    private static String outputName;
    private static ChatEndpoint chat;

    public static void installOutput(String outputName) throws InstantiationException{
        OutputFactory.outputName = outputName;
        if(outputName.equals(Constants.INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT)){
            chat = new ChatEndpointConfigurator().getEndpointInstance(ChatEndpoint.class);
        }
    }

    public static void println(String message){
        switch (outputName){
            case Constants.INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT:
                Set<Session> sessions = chat.getUserSessions();
                sessions.forEach(session -> {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                });
                break;
            case Constants.INIT_PARAM_VALUE_SYSTEM:
                System.out.println(message);
                break;
        }
    }

}
