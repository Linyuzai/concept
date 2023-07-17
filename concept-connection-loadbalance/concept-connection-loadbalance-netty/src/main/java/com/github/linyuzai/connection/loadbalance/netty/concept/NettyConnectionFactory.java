package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;
import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * Netty 连接工厂。
 * <p>
 * Netty connection factory.
 */
public class NettyConnectionFactory extends AbstractConnectionFactory<NettyConnection> {

    public NettyConnectionFactory() {
        addScopes(NettyScoped.NAME);
    }

    @Override
    public boolean support(Object o, Map<Object, Object> metadata, ConnectionLoadBalanceConcept concept) {
        return o instanceof ChannelHandlerContext || o instanceof Channel;
    }

    @Override
    protected AbstractConnection doCreate(Object o, ConnectionLoadBalanceConcept concept) {
        return new NettyConnection(getChannel(o));
    }

    protected Channel getChannel(Object o) {
        if (o instanceof ChannelHandlerContext) {
            return ((ChannelHandlerContext) o).channel();
        } else if (o instanceof Channel) {
            return (Channel) o;
        } else {
            throw new IllegalArgumentException("Can not happen");
        }
    }
}
