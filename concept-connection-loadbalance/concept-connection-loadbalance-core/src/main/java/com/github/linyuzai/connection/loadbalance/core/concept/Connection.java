package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;

import java.net.URI;
import java.util.Map;

public interface Connection {

    Object getId();

    void setType(String type);

    String getType();

    URI getUri();

    Map<Object, Object> getMetadata();

    void setMessageEncoder(MessageEncoder encoder);

    MessageEncoder getMessageEncoder();

    void setMessageDecoder(MessageDecoder decoder);

    MessageDecoder getMessageDecoder();

    void setConcept(ConnectionLoadBalanceConcept concept);

    ConnectionLoadBalanceConcept getConcept();

    void send(Message message);

    void close();

    void close(String reason);

    void close(int code, String reason);

    boolean isAlive();

    void setAlive(boolean alive);

    long getLastHeartbeat();

    void setLastHeartbeat(long lastHeartbeat);

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

        /**
         * 未定义的类型
         * 用于无法区分的连接类型
         */
        @Deprecated
        public static final String UNDEFINED = "Connection@undefined";
    }
}
