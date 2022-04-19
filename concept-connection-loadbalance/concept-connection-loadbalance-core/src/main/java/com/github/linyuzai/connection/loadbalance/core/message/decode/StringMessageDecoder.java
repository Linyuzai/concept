package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.StringMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class StringMessageDecoder implements MessageDecoder {

    @NonNull
    private Charset charset;

    public StringMessageDecoder() {
        this(StandardCharsets.UTF_8);
    }

    @Override
    public Message decode(byte[] message) {
        return new StringMessage(new String(message, charset));
    }
}
