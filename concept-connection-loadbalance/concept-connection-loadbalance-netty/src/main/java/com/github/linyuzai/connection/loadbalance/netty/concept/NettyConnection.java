package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

@Getter
public class NettyConnection extends AbstractConnection {

    private final Channel channel;

    public NettyConnection(Channel channel,@NonNull String type) {
        super(type);
        this.channel = channel;
    }

    public NettyConnection(Channel channel,@NonNull String type, Map<Object, Object> metadata) {
        super(type, metadata);
        this.channel = channel;
    }

    @Override
    public void ping(PingMessage ping) {

    }

    @Override
    public void pong(PongMessage pong) {

    }

    @Override
    public void doClose(Object reason) {
        channel.close();
    }

    @Override
    public Object getCloseReason(int code, String reason) {
        return reason;
    }

    @Override
    public void doSend(Object message) {
        channel.writeAndFlush(message);
    }

    @Override
    public Object getId() {
        return channel.id();
    }

    @Override
    public void close(String reason) {
        channel.close();
    }
}
