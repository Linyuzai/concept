package com.github.linyuzai.connection.loadbalance.core.message.idempotent;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;

/**
 * 消息幂等校验器工厂实现。
 * <p>
 * Factory of idempotent verifier impl in memory.
 */
public class InMemoryMessageIdempotentVerifierFactory extends AbstractScopedFactory<MessageIdempotentVerifier>
        implements MessageIdempotentVerifierFactory {

    @Override
    public MessageIdempotentVerifier create(String scope) {
        return new InMemoryMessageIdempotentVerifier();
    }
}
