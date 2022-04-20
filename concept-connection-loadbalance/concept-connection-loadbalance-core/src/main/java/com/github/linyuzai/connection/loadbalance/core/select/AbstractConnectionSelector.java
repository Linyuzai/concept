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
    public Connection select(Message message, Collection<Connection> connections) {
        List<Connection> list = new ArrayList<>();
        List<Connection> proxyList = new ArrayList<>();
        for (Connection connection : connections) {
            if (connection.hasProxyFlag()) {
                proxyList.add(connection);
            } else {
                list.add(connection);
            }
        }
        Connection select;
        if (list.isEmpty()) {
            select = null;
        } else {
            select = doSelect(message, list);
        }

        if (message.getHeaders().containsKey(Message.FORWARD)) {
            //已经被其他服务转发的就不再转发
            return select;
        }

        if (select == null) {
            //没有对应的连接，直接进行转发
            return Connections.of(proxyList);
        }

        if (broadcast) {
            //广播
            List<Connection> combine = new ArrayList<>(proxyList);
            combine.add(0, select);
            return Connections.of(combine);
        } else {
            //单播
            return select;
        }

    }

    public abstract Connection doSelect(Message message, Collection<Connection> connections);
}
