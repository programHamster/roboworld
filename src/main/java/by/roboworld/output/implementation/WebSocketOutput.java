package by.roboworld.output.implementation;

import by.roboworld.output.Output;
import by.roboworld.websocket.ChatEndpoint;
import by.roboworld.websocket.ChatEndpointConfigurator;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Implementation for write a message through <code>WebSocket</code>
 */
public class WebSocketOutput extends Output {
    private ChatEndpoint chat = new ChatEndpointConfigurator().getEndpointInstance(ChatEndpoint .class);

    @Override
    public void write(String message) throws IOException {
        if(!Thread.currentThread().isInterrupted()){
            for(Session session : chat.getUserSessions()){
                session.getBasicRemote().sendText(message);
            }
        }
    }

    @Override
    public void close(){
        chat = null;
    }
}
