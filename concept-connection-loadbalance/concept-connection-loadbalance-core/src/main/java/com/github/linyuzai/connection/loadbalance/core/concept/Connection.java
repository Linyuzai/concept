package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;

import java.util.Map;

public interface Connection {

    String URI = "uri";

    Object getId();

    String getType();

    Map<Object, Object> getMetadata();

    MessageEncoder getMessageEncoder();

    MessageDecoder getMessageDecoder();

    void send(Message message);

    void close();

    class Type {

        /**
         * 普通的连接
         */
        public static final String CLIENT = "Connection@client";

        /**
         * 用于监听其他服务的连接
         * 接收转发的消息
         */
        public static final String SUBSCRIBER = "Connection@subcriber";

        /**
         * 用于被其他服务监听的连接
         * 转发消息
         */
        public static final String OBSERVABLE = "Connection@observable";
    }
}
