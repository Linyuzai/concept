package com.github.linyuzai.connection.loadbalance.autoconfigure.logger;

import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLogger;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoggerFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionLoggerFactoryImpl extends AbstractScopedFactory<ConnectionLogger>
        implements ConnectionLoggerFactory {

    private String tag;

    @Override
    public ConnectionLogger create(String scope) {
        return new ConnectionLoggerImpl(tag);
    }
}
