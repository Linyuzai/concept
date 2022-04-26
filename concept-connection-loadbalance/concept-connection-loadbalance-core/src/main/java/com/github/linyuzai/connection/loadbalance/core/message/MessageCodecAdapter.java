package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;

public interface MessageCodecAdapter {

    MessageEncoder getMessageEncoder(String type);

    MessageDecoder getMessageDecoder(String type);
}
