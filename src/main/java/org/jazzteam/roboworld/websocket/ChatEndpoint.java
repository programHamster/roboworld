package org.jazzteam.roboworld.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value="/chat", configurator = ChatEndpointConfigurator.class)
public class ChatEndpoint {

    private final Set<Session> userSessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session userSession){
        userSessions.add(userSession);
    }

    @OnClose
    public void onClose(Session userSession){
        userSessions.remove(userSession);
    }

    public Set<Session> getUserSessions(){
        return Collections.unmodifiableSet(userSessions);
    }

}
