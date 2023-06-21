package com.github.linyuzai.connection.loadbalance.core.message;

public interface MessageCondition {

    void apply(Message message);
}
