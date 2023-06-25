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
    public void ping(PingMessage ping) {

    }

    @Override
    public void pong(PongMessage pong) {

    }

    @Override
    public void doClose(Object reason) {
        if (closeCallback != null) {
            closeCallback.accept(reason);
        }
    }

    @Override
    public Object getCloseReason(int code, String reason) {
        return reason;
    }

    @Override
    public void close(String reason) {
        doClose(reason);
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
