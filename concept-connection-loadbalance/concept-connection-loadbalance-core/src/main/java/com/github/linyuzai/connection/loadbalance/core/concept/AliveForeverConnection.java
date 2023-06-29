package com.github.linyuzai.connection.loadbalance.core.concept;

import com.github.linyuzai.connection.loadbalance.core.message.PingMessage;
import com.github.linyuzai.connection.loadbalance.core.message.PongMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class AliveForeverConnection extends AbstractConnection {

    private Consumer<Object> closeCallback;

    public AliveForeverConnection(@NonNull String type) {
        super(type);
    }

    public AliveForeverConnection(@NonNull String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void doPing(PingMessage message, Runnable success, Consumer<Throwable> error, Runnable onComplete) {

    }

    @Override
    public void doPong(PongMessage message, Runnable success, Consumer<Throwable> error, Runnable onComplete) {

    }

    @Override
    public void doClose(Object reason, Runnable success, Consumer<Throwable> error, Runnable onComplete) {
        if (closeCallback != null) {
            try {
                closeCallback.accept(reason);
                success.run();
            } catch (Throwable e) {
                error.accept(e);
            }
        }
        onComplete.run();
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public long getLastHeartbeat() {
        return System.currentTimeMillis();
    }
}
