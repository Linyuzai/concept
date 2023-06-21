package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.LifecycleListener;
import com.github.linyuzai.connection.loadbalance.core.extension.UserMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.UserSelector;

/**
 * userId 注册器
 * <p>
 * 配合 {@link UserMessage} {@link UserSelector} 使用
 */
public class DefaultEndpointUserMetadataRegister implements LifecycleListener, WebSocketScoped {

    public static final String NAME = "userId";

    @Override
    public void onEstablish(Connection connection) {
        String userId = ((WebSocketConnection) connection).getQueryParameter(NAME);
        connection.getMetadata().put(UserSelector.KEY, userId);
    }

    @Override
    public void onClose(Connection connection, Object reason) {

    }
}
