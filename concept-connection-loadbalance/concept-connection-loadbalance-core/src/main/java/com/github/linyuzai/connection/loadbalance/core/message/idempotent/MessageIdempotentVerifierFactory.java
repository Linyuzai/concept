package com.github.linyuzai.connection.loadbalance.core.message.idempotent;

import com.github.linyuzai.connection.loadbalance.core.scope.ScopedFactory;

/**
 * 消息幂等校验器工厂。
 * <p>
 * Factory of idempotent verifier.
 */
public interface MessageIdempotentVerifierFactory extends ScopedFactory<MessageIdempotentVerifier> {

}
