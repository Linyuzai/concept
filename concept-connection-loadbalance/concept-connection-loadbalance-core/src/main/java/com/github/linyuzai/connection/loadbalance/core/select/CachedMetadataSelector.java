/*
package com.github.linyuzai.connection.loadbalance.core.select;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.exception.ConnectionLoadBalanceException;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public abstract class CachedMetadataSelector implements ConnectionSelector {

    private final MetadataSelector selector;

    @Override
    public boolean support(Message message) {
        return selector.support(message);
    }

    @Override
    public Connection select(Message message, ConnectionServer server, Collection<Connection> clients, Collection<Connection> observables, Collection<Connection> subscribers) {
        Connection hit = hit(message, server, clients, observables, subscribers);
        return hit == null ? selector.select(message, server, clients, observables, subscribers) : hit;
    }

    public Connection hit(Message message, ConnectionServer server, Collection<Connection> clients, Collection<Connection> observables, Collection<Connection> subscribers) {
        if (message.getHeaders().containsKey(Message.HIT)) {
            //已经被其他服务命中为当前服务
            return selector.doSelect(message, clients);
        }
        String instanceId = getInstanceIds(message);
        if (instanceId == null) {
            //未命中
            return null;
        }
        if (server.getInstanceId().equals(instanceId)) {
            //命中当前服务
            return doHit(message, clients);
        }
        for (Connection observable : observables) {
            ConnectionServer cs = (ConnectionServer) observable.getMetadata().get(ConnectionServer.class);
            if (cs == null) {
                continue;
            }
            if (cs.getInstanceId().equals(instanceId)) {
                message.getHeaders().put(Message.HIT, instanceId);
                return observable;
            }
        }
        throw new ConnectionLoadBalanceException("No observable hit");
    }

    public Connection doHit(Message message, Collection<Connection> clients) {
        Connection connection = selector.doSelect(message, clients);
        if (connection == null) {
            throw new ConnectionLoadBalanceException("No connection hit");
        }
        return connection;
    }

    public Collection<String> getInstanceIds(Message message) {
        String key = message.getHeaders().get(selector.getName());
        return getCache(key);
    }

    public abstract Collection<String> getCache(String key);
}
*/
