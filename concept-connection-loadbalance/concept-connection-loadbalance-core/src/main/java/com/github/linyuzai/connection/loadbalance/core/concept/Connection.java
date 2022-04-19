package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.proxy.ConnectionProxy;
import com.github.linyuzai.connection.loadbalance.core.proxy.ProxyMarker;

import java.util.Map;

public interface Connection extends ProxyMarker {

    Object getId();

    Map<String, String> getMetadata();

    MessageEncoder getMessageEncoder();

    MessageDecoder getMessageDecoder();

    void send(Message message);

    void close();

    @Override
    default boolean isProxy() {
        return getMetadata().containsKey(ConnectionProxy.FLAG);
    }
}
