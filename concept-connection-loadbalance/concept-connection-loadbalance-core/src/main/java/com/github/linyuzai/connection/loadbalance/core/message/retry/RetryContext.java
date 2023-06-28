package com.github.linyuzai.connection.loadbalance.core.message.retry;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RetryContext {

    private final Connection connection;

    private final Message message;

    private final Throwable error;


}
