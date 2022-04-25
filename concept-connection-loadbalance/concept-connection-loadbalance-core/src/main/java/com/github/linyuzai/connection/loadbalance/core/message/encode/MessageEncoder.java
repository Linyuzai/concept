package com.github.linyuzai.connection.loadbalance.core.message.encode;

import com.github.linyuzai.connection.loadbalance.core.message.Message;

public interface MessageEncoder {

    Object encode(Message message);
}
