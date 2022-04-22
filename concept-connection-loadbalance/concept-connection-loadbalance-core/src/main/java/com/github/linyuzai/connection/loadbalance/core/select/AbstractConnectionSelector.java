package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.Connections;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public abstract class AbstractConnectionSelector implements ConnectionSelector {

    private boolean broadcast;

    @Override
    public ConnectionSelector broadcast(boolean broadcast) {
        this.broadcast = broadcast;
        return this;
    }

    @Override
    public Connection select(Message message, Collection<Connection> clients, Collection<Connection> observables) {
        Connection select;
        if (clients.isEmpty()) {
            select = null;
        } else {
            select = doSelect(message, clients);
        }

        if (message.getHeaders().containsKey(Message.FORWARD)) {
            //已经被其他服务转发的就不再转发
            return select;
        }

        if (select == null) {
            //没有对应的连接，直接进行转发
            return Connections.of(observables);
        }

        if (broadcast) {
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
