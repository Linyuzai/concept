package com.github.linyuzai.connection.loadbalance.core.message.idempotent;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 消息幂等校验器实现。
 * <p>
 * Verify idempotent of message in memory.
 */
@RequiredArgsConstructor
public class InMemoryMessageIdempotentVerifier implements MessageIdempotentVerifier {

    private final Map<String, Long> ids = new ConcurrentHashMap<>();

    private volatile boolean once = true;

    @Getter
    private final long period;

    @Getter
    private final long timeout;

    @Override
    public boolean verify(Message message, ConnectionLoadBalanceConcept concept) {
        if (once) {
            synchronized (this) {
                if (once) {
                    once = false;
                    concept.getScheduledExecutor()
                            .scheduleAtFixedRate(this::removeTimeout,
                                    period, period, TimeUnit.MILLISECONDS);
                }
            }
        }
        String id = message.getId();
        if (id == null) {
            return true;
        }
        Holder holder = new Holder();
        ids.computeIfAbsent(id, k -> {
            //如果不存在则会回调进入，说明消息没有消费过，并添加这个消息ID（同步的）
            //If message id is not duplicate and put this message id in sync
            holder.verified = true;
            return System.currentTimeMillis();
        });
        return holder.verified;
    }

    protected void removeTimeout() {
        ids.entrySet().removeIf(next -> System.currentTimeMillis() - next.getValue() > timeout);
    }

    static class Holder {

        boolean verified = false;
    }
}
