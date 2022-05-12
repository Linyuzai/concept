package com.github.linyuzai.connection.loadbalance.netty.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceConcept;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.LinkedHashMap;

@AllArgsConstructor
public class WebSocketLoadBalanceHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    @NonNull
    private NettyLoadBalanceConcept concept;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
        concept.onMessage(ctx.channel().id(), Connection.Type.CLIENT, msg.content().array());
    }

    @Override
    public void channelActive(@NonNull ChannelHandlerContext ctx) throws Exception {
        concept.onOpen(ctx.channel(), new LinkedHashMap<>());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(@NonNull ChannelHandlerContext ctx) throws Exception {
        concept.onClose(ctx.channel().id(), Connection.Type.CLIENT, null);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        concept.onError(ctx.channel().id(), Connection.Type.CLIENT, cause);
        super.exceptionCaught(ctx, cause);
    }
}
