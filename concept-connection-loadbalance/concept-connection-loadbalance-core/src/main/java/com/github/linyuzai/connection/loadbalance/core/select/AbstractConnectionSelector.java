package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import com.github.linyuzai.connection.loadbalance.core.repository.ConnectionRepository;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/***
 * 连接选择器的抽象类
 */
@Getter
public abstract class AbstractConnectionSelector implements ConnectionSelector {

    @Override
    public Collection<Connection> select(Message message, ConnectionRepository repository, ConnectionLoadBalanceConcept concept) {
        Collection<Connection> clients = repository.select(Connection.Type.CLIENT);
        Collection<Connection> select;
        if (clients.isEmpty()) {
            select = Collections.emptyList();
        } else {
            select = doSelect(message, clients);
        }

        if (message instanceof PingMessage || message instanceof PongMessage) {
            //ping pong 不转发
            return select;
        }

        if (message.isForward()) {
            //已经被其他服务转发的就不再转发
            return select;
        }
        //添加转发标记，防止其他服务再次转发
        message.setForward(true);

        Collection<Connection> observables = repository.select(Connection.Type.OBSERVABLE);

        if (select == null || select.isEmpty()) {
            //没有对应的连接，直接进行转发
            return observables;
        }

        if (message.isBroadcast()) {
            //广播
            List<Connection> combine = new ArrayList<>(select.size() + observables.size());
            combine.addAll(select);
            combine.addAll(observables);
            return combine;
        } else {
            //单播
            return select;
        }
    }

    public abstract Collection<Connection> doSelect(Message message, Collection<Connection> connections);
}
