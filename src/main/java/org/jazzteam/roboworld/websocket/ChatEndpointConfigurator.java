package org.jazzteam.roboworld.websocket;

import javax.websocket.server.ServerEndpointConfig.Configurator;

public class ChatEndpointConfigurator extends Configurator {

    private static ChatEndpoint chat = new ChatEndpoint();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException{
        return (T)chat;
    }

}
