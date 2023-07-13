package com.github.linyuzai.connection.loadbalance.core.message.idempotent;

import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScopedFactory;

public class MessageIdempotentVerifierFactoryImpl extends AbstractScopedFactory<MessageIdempotentVerifier>
        implements MessageIdempotentVerifierFactory {

    @Override
    public MessageIdempotentVerifier create(String scope) {
        return new MessageIdempotentVerifierImpl();
    }
}
