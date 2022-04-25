package com.github.linyuzai.connection.loadbalance.netty.handler;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.BinaryMessage;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.MessageHandler;
import com.github.linyuzai.connection.loadbalance.core.subscribe.SubscribeMessage;

public interface RedefinedTypeMessageHandler extends MessageHandler {

    @Override
    default void onMessage(Message message, Connection connection) {
        if (message instanceof BinaryMessage) {
            byte[] payload = message.getPayload();
            try {
                SubscribeMessage subscribeMessage = tryDecode(payload);
                connection.redefineType(Connection.Type.OBSERVABLE, () -> {
                    onObservableTypeRedefined(connection);
                });
                connection.getConcept()
                        .subscribe(subscribeMessage.getConnectionServer(), false);
                return;
            } catch (Throwable ignore) {
            }
        }
        onClientMessage(message, connection);
    }

    SubscribeMessage tryDecode(byte[] bytes);

    void onObservableTypeRedefined(Connection connection);

    void onClientMessage(Message message, Connection connection);
}
