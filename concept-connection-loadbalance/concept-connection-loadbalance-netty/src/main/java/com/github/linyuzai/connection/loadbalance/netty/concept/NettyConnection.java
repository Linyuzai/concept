package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Netty 连接。
 * <p>
 * Netty connection.
 */
@Getter
@RequiredArgsConstructor
public class NettyConnection extends AbstractConnection {

    private final Channel channel;

    @Override
    public Object getId() {
        return channel.id();
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        channel.writeAndFlush(message).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                onSuccess.run();
            } else {
                Throwable cause = future.cause();
                if (cause instanceof IOException) {
                    onError.accept(new MessageTransportException(cause));
                } else {
                    onError.accept(cause);
                }
            }
            onComplete.run();
        });
    }

    @Override
    public void doPing(PingMessage message, Runnable success, Consumer<Throwable> error, Runnable onComplete) {

    }

    @Override
    public void doPong(PongMessage message, Runnable success, Consumer<Throwable> error, Runnable onComplete) {

    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        channel.close().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                onSuccess.run();
            } else {
                onError.accept(future.cause());
            }
            onComplete.run();
        });
    }
}
