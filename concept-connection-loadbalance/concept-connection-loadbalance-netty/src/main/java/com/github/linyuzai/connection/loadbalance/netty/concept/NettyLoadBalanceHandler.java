package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.extension.GroupSelector;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Netty 负载均衡处理器。
 * <p>
 * Netty load balance handler.
 */
@Getter
@RequiredArgsConstructor
public class NettyLoadBalanceHandler extends ChannelInboundHandlerAdapter {

    private final NettyLoadBalanceConcept concept;

    private final Map<Object, Object> metadata;

    public NettyLoadBalanceHandler(NettyLoadBalanceConcept concept) {
        this(concept, (Map<Object, Object>) null);
    }

    public NettyLoadBalanceHandler(NettyLoadBalanceConcept concept, String group) {
        this(concept, getGroupMetadata(group));
    }

    private static Map<Object, Object> getGroupMetadata(String group) {
        Map<Object, Object> map = new HashMap<>();
        map.put(GroupSelector.KEY, group);
        return map;
    }

    protected Connection create(ChannelHandlerContext ctx, Map<Object, Object> metadata) {
        return concept.createConnection(ctx, metadata);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        concept.onEstablish(create(ctx, metadata));
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        concept.onClose(ctx.channel().id(), Connection.Type.CLIENT, "Inactive");
        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        concept.onError(ctx.channel().id(), Connection.Type.CLIENT, cause);
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        concept.onMessage(ctx.channel().id(), Connection.Type.CLIENT, msg);
        ReferenceCountUtil.release(msg);
        //ctx.fireChannelRead(msg);
    }
}
