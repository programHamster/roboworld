package org.jazzteam.roboworld.websocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * This class sets a single endpoint for all web socket connections.
 */
public class ChatEndpointConfigurator extends Configurator {

    /** the single endpoint */
    private static ChatEndpoint chat = new ChatEndpoint();

    /**
     * Returns a single endpoint for all web socket connections.
     *
     * @param endpointClass the class of the endpoint
     * @param <T> the type of the endpoint
     * @return a single endpoint for all web socket connections
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        return (T)chat;
    }

}
