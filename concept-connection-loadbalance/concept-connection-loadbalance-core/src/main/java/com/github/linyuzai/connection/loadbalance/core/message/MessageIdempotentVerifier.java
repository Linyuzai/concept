package com.github.linyuzai.connection.loadbalance.core.message;

public interface MessageIdempotentVerifier {

    MessageIdempotentVerifier VERIFIED = message -> true;

    boolean verify(Message message);
}
