package com.github.linyuzai.connection.loadbalance.netty.websocket;

import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyConnection;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;

import java.util.function.Consumer;

public class WebSocketNettyConnection extends NettyConnection {

    public WebSocketNettyConnection(NettyConnection connection) {
        super(connection.getChannel());
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        getChannel().writeAndFlush(new PingWebSocketFrame(Unpooled.wrappedBuffer(message.getPayload())))
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        onSuccess.run();
                    } else {
                        Throwable cause = future.cause();
                        onError.accept(cause);
                    }
                    onComplete.run();
                });
    }

    @Override
    public void doPong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        getChannel().writeAndFlush(new PongWebSocketFrame(Unpooled.wrappedBuffer(message.getPayload())))
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        onSuccess.run();
                    } else {
                        Throwable cause = future.cause();
                        onError.accept(cause);
                    }
                    onComplete.run();
                });
    }
}
