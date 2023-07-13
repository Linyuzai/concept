package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NettyLoadBalanceConcept extends AbstractConnectionLoadBalanceConcept {

    public static final String ID = "netty";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void onDestroy() {
        for (String type : connectionRepository.types()) {
            for (Connection connection : connectionRepository.select(type)) {
                connection.close(Connection.Close.SERVER_STOP);
            }
        }
    }

    public static class Builder extends AbstractBuilder<Builder, NettyLoadBalanceConcept> {

        @Override
        protected String getScope() {
            return NettyScoped.NAME;
        }

        @Override
        protected NettyLoadBalanceConcept create() {
            return new NettyLoadBalanceConcept();
        }
    }
}
