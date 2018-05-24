package by.roboworld.websocket;

import by.roboworld.Constants;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the subscription of users to receive messages from the server.
 */
@ServerEndpoint(value= Constants.MESSAGE_URL, configurator = ChatEndpointConfigurator.class)
public class ChatEndpoint {

    /** stores signed user sessions */
    private final Set<Session> userSessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session userSession){
        userSessions.add(userSession);
    }

    @OnClose
    public void onClose(Session userSession){
        userSessions.remove(userSession);
    }

    /**
     * Returns an unmodifiable set of user sessions.
     *
     * @return an unmodifiable set of user sessions
     */
    public Set<Session> getUserSessions(){
        return Collections.unmodifiableSet(userSessions);
    }

}
