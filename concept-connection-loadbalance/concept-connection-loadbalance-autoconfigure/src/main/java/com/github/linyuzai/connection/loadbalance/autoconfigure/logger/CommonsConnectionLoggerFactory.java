package com.github.linyuzai.connection.loadbalance.autoconfigure.logger;

import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLogger;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLoggerFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * Commons日志工厂。
 * <p>
 * Factory of commons logger.
 */
@Getter
@Setter
public class CommonsConnectionLoggerFactory extends AbstractScopedFactory<ConnectionLogger>
        implements ConnectionLoggerFactory {

    private String tag;

    @Override
    public ConnectionLogger create(String scope) {
        return new CommonsConnectionLogger(tag);
    }
}
