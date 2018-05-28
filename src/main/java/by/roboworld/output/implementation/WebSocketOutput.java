package by.roboworld.output.implementation;

import by.roboworld.output.Output;
import by.roboworld.websocket.ChatEndpoint;
import by.roboworld.websocket.ChatEndpointConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

/**
 * Implementation for write a message through <code>WebSocket</code>.
 */
public class WebSocketOutput extends Output {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketOutput.class);

    private ChatEndpoint chat = new ChatEndpointConfigurator()
            .getEndpointInstance(ChatEndpoint.class);

    /**
     * Writes the message. This method is synchronized because an
     * <code>java.lang.IllegalStateException</code> exception is thrown when a
     * message is sent to the same session at the same time.
     *
     * @param message the message
     */
    @Override
    public synchronized void write(final String message) {
        LOGGER.info("writing message : {}", message);
        final long timeout = 2000;
        if (!Thread.currentThread().isInterrupted()) {
            for (Session session : chat.getUserSessions()) {
                // double check: ensure session is still open
                if (session.isOpen()) {
                    RemoteEndpoint.Async async = session.getAsyncRemote();
                    async.setSendTimeout(timeout);
                    async.sendText(message, result -> {
                        if (!result.isOK()) {
                            LOGGER.error("Message \"" + message +
                                    "\" don't sent", result.getException());
                        }
                    });
                }
            }
        }
    }

    @Override
    public void close() {
        LOGGER.debug("WebSocketOutput closed");
        chat = null;
    }
}
