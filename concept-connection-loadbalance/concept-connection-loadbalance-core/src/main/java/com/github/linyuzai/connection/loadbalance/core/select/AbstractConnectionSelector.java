package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.utils.Connections;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public abstract class AbstractConnectionSelector implements ConnectionSelector {

    @Override
    public Connection select(Message message, ConnectionRepository repository, ConnectionLoadBalanceConcept concept) {
        Collection<Connection> clients = repository.select(Connection.Type.CLIENT);
        Connection select;
        if (clients.isEmpty()) {
            select = null;
        } else {
            select = doSelect(message, clients);
        }

        if (message instanceof PingMessage || message instanceof PongMessage) {
            //ping pong 不转发
            return select;
        }

        if (message.getHeaders().containsKey(Message.FORWARD)) {
            //已经被其他服务转发的就不再转发
            return select;
        }

        Collection<Connection> observables = repository.select(Connection.Type.OBSERVABLE);

        if (select == null) {
            //没有对应的连接，直接进行转发
            return Connections.of(observables);
        }

        String broadcast = message.getHeaders()
                .getOrDefault(Message.BROADCAST, Boolean.TRUE.toString());

        if (Boolean.parseBoolean(broadcast)) {
            //广播
            List<Connection> combine = new ArrayList<>(observables);
            combine.add(0, select);
            return Connections.of(combine);
        } else {
            //单播
            return select;
        }
    }

    public abstract Connection doSelect(Message message, Collection<Connection> connections);
}
