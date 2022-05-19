package com.github.linyuzai.connection.loadbalance.core.extension;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.ObjectMessage;

public class UserIdMessage extends ObjectMessage {

    public UserIdMessage(Object payload, String... userIds) {
        super(payload);
        getHeaders().put(Message.BROADCAST, Boolean.FALSE.toString());
        getHeaders().put(UserIdSelector.KEY, String.join(",", userIds));
    }
}
