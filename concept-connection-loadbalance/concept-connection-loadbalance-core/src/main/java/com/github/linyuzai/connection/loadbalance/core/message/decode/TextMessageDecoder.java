package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.TextMessage;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.nio.ByteBuffer;
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
    public Message decode(Object message) {
        if (message instanceof String) {
            return new TextMessage((String) message);
        } else if (message instanceof byte[]) {
            return new TextMessage(new String((byte[]) message, charset));
        } else if (message instanceof ByteBuffer) {
            return new TextMessage(new String(((ByteBuffer) message).array(), charset));
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
