package com.github.linyuzai.connection.loadbalance.netty.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyConnection;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * Netty WebSocket 负载均衡处理器。
 * <p>
 * Netty WebSocket load balance handler.
 */
public class WebSocketNettyLoadBalanceHandler extends NettyLoadBalanceHandler {

    public WebSocketNettyLoadBalanceHandler(NettyLoadBalanceConcept concept, Map<Object, Object> metadata) {
        super(concept, metadata);
    }

    public WebSocketNettyLoadBalanceHandler(NettyLoadBalanceConcept concept) {
        super(concept);
    }

    public WebSocketNettyLoadBalanceHandler(NettyLoadBalanceConcept concept, String group) {
        super(concept, group);
    }

    @Override
    protected Connection create(ChannelHandlerContext ctx, Map<Object, Object> metadata) {
        Connection connection = super.create(ctx, metadata);
        return new WebSocketNettyConnection((NettyConnection) connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //TODO ContinuationWebSocketFrame
        super.channelRead(ctx, msg);
    }
}
