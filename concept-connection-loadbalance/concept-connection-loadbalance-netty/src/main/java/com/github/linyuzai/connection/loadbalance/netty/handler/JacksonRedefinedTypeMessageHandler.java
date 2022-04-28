/*
package com.github.linyuzai.connection.loadbalance.netty.handler;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.JacksonSubscribeMessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.SubscribeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class JacksonRedefinedTypeMessageHandler implements RedefinedTypeMessageHandler {

    private final MessageEncoder messageEncoder;

    private final MessageDecoder messageDecoder;

    public JacksonRedefinedTypeMessageHandler() {
        this(new JacksonSubscribeMessageEncoder(), new JacksonSubscribeMessageDecoder());
    }

    @Override
    public SubscribeMessage tryDecode(byte[] bytes) {
        //Message message = messageDecoder.decode(bytes);
        //return (SubscribeMessage) message;
    }

    @Override
    public void onObservableTypeRedefined(Connection connection) {
        if (connection instanceof AbstractConnection) {
            //((AbstractConnection) connection).setMessageEncoder(messageEncoder);
            //((AbstractConnection) connection).setMessageDecoder(messageDecoder);
        }
    }
}
*/
