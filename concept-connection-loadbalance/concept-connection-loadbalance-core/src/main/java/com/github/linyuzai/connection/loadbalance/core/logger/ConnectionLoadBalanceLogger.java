package com.github.linyuzai.connection.loadbalance.core.logger;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;
import com.github.linyuzai.connection.loadbalance.core.server.ConnectionServer;
import lombok.AllArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 日志支持类
 */
@AllArgsConstructor
public class ConnectionLoadBalanceLogger extends AbstractScoped {

    private Consumer<String> info;

    private BiConsumer<String, Throwable> error;

    public String getServer(ConnectionServer server) {
        if (server == null) {
            return "Unknown server";
        }
        return server.getServiceId() + ":" + server.getHost() + ":" + server.getPort();
    }

    public String appendTag(String msg) {
        return "LBWebSocket >> " + msg;
    }

    public void info(String msg) {
        info.accept(appendTag(msg));
    }

    public void error(String msg, Throwable e) {
        error.accept(appendTag(msg), e);
    }
}
