package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class NettyLoadBalanceHandler extends ChannelInboundHandlerAdapter {

    private final NettyLoadBalanceConcept concept;

    private final Map<Object, Object> metadata;

    public NettyLoadBalanceHandler(NettyLoadBalanceConcept concept) {
        this(concept, null);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        concept.onEstablish(ctx, metadata);
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
