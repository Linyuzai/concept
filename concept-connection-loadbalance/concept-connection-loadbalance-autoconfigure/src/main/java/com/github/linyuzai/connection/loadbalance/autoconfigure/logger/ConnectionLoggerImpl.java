package com.github.linyuzai.connection.loadbalance.autoconfigure.logger;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.logger.ConnectionLogger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日志实现。
 * <p>
 * Impl of {@link ConnectionLogger}.
 */
@Getter
@RequiredArgsConstructor
public class ConnectionLoggerImpl implements ConnectionLogger {

    private final Log log = LogFactory.getLog(ConnectionLogger.class);

    /**
     * 前缀标签
     * <p>
     * Prefix label
     */
    private final String tag;

    @Override
    public void info(String msg, ConnectionLoadBalanceConcept concept) {
        log.info(tag + msg);
    }

    @Override
    public void error(String msg, Throwable e, ConnectionLoadBalanceConcept concept) {
        log.error(tag + msg, e);
    }
}
