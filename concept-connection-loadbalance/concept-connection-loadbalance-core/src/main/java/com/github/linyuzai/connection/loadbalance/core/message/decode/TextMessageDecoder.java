package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.TextMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class TextMessageDecoder implements MessageDecoder {

    @NonNull
    private Charset charset;

    public TextMessageDecoder() {
        this(StandardCharsets.UTF_8);
    }

    @Override
    public Message decode(byte[] message) {
        return new TextMessage(new String(message, charset));
    }
}
