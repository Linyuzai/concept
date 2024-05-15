package com.github.linyuzai.connection.loadbalance.core.message.sender;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 并行的消息发送器。
 * <p>
 * Message sender with {@link CompletableFuture}
 */
@Getter
@RequiredArgsConstructor
public class CompletableFutureMessageSender implements MessageSender {

    private final Executor executor;

    @Override
    public void send(Collection<? extends Runnable> senders, ConnectionLoadBalanceConcept concept) {
        CompletableFuture<?>[] futures = senders.stream().map(it -> {
            if (executor == null) {
                return CompletableFuture.runAsync(it);
            } else {
                return CompletableFuture.runAsync(it, executor);
            }
        }).toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
    }
}
