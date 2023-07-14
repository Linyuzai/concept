package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 连接选择器的抽象类。
 * <p>
 * Abstract selector of connections.
 */
@Getter
public abstract class AbstractConnectionSelector extends AbstractScoped implements ConnectionSelector {

    @Override
    public Collection<Connection> select(Message message, ConnectionLoadBalanceConcept concept) {
        ConnectionRepository repository = concept.getConnectionRepository();
        Collection<Connection> clients = repository.select(Connection.Type.CLIENT);
        Collection<Connection> select;
        if (clients.isEmpty()) {
            select = Collections.emptyList();
        } else {
            select = doSelect(message, clients, concept);
        }

        if (message instanceof PingMessage || message instanceof PongMessage) {
            //ping pong 不转发
            //Not forward ping and pong
            return select;
        }

        if (!message.needForward()) {
            //已经被其他服务转发的就不再转发
            //Not forward if it is forwarded and has the flag in headers
            return select;
        }

        Collection<Connection> observables = repository.select(Connection.Type.OBSERVABLE);

        if (select == null || select.isEmpty()) {
            //没有对应的连接，直接进行转发
            //Forward if not found any client connections
            return observables;
        }

        if (message.needBroadcast()) {
            //广播
            //Forward if need broadcast
            List<Connection> combine = new ArrayList<>(select.size() + observables.size());
            combine.addAll(select);
            combine.addAll(observables);
            return combine;
        } else {
            //单播
            //If set broadcast is true and client connections is not empty
            return select;
        }
    }

    /**
     * 选择普通的客户端连接。
     * <p>
     * Select connections from the client type.
     */
    public abstract Collection<Connection> doSelect(Message message,
                                                    Collection<Connection> connections,
                                                    ConnectionLoadBalanceConcept concept);
}
