package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import com.github.linyuzai.connection.loadbalance.core.subscribe.ProxyMarker;

import java.util.Map;

public interface Connection extends ProxyMarker {

    Object getId();

    Map<Object, Object> getMetadata();

    MessageEncoder getMessageEncoder();

    MessageDecoder getMessageDecoder();

    void send(Message message);

    void close();

    @Override
    default boolean hasProxyFlag() {
        return getMetadata().containsKey(ProxyMarker.FLAG);
    }

    enum Type {

        /**
         * 普通的连接
         */
        CLIENT,

        /**
         * 用于监听其他服务的连接
         * 接收转发的消息
         */
        SUBSCRIBER,

        /**
         * 用于被其他服务监听的连接
         * 转发消息
         */
        OBSERVABLE
    }
}
