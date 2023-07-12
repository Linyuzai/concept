package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * 未知连接。
 * 表示在连接仓库中未找到的连接。
 * <p>
 * Connection which absent in connection repository.
 */
@Getter
public class UnknownConnection extends AbstractConnection {

    private final Object id;

    public UnknownConnection(Object id, String type, ConnectionLoadBalanceConcept concept) {
        setType(type);
        setConcept(concept);
        this.id = id;
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doPing(PingMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doPong(PongMessage message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }

    @Override
    public void doClose(Object reason, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        onComplete.run();
    }
}
