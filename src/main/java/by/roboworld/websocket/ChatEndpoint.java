package by.roboworld.websocket;

import by.roboworld.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the subscription of users to receive messages from the
 * server.
 */
@ServerEndpoint(value = Constants.MESSAGE_URL, configurator = ChatEndpointConfigurator.class)
public class ChatEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatEndpoint.class);

    /** Stores signed user sessions. */
    private final Set<Session> userSessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session userSession) {
        LOGGER.info("Session {} subscribed", userSession.getId());
        userSessions.add(userSession);
    }

    @OnClose
    public void onClose(Session userSession) {
        LOGGER.info("Session {} unsubscribed", userSession.getId());
        userSessions.remove(userSession);
    }

    /**
     * Returns an unmodifiable set of user sessions.
     *
     * @return an unmodifiable set of user sessions
     */
    public Set<Session> getUserSessions() {
        return Collections.unmodifiableSet(userSessions);
    }

}
