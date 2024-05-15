package com.github.linyuzai.connection.loadbalance.core.message.sender;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executor;

/**
 * 并行消息发送器的工厂。
 * <p>
 * Factory of {@link CompletableFutureMessageSender}
 */
@Getter
@Setter
public class CompletableFutureMessageSenderFactory extends AbstractMessageSenderFactory {

    private Executor executor;

    @Override
    public MessageSender create(String scope) {
        return new CompletableFutureMessageSender(executor);
    }
}
