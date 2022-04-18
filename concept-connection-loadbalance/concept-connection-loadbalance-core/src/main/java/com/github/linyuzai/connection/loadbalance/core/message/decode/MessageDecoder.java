package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.message.Message;

public interface MessageDecoder {

    Message decode(byte[] message);
}
