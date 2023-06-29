package com.github.linyuzai.connection.loadbalance.core.message;

import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MessageCodecAdapterChain implements MessageCodecAdapter {

    private final List<MessageCodecAdapter> messageCodecAdapters;

    @Override
    public MessageEncoder getMessageEncoder(String type, MessageEncoder encoder) {
        return adaptMessageEncoder(0, type, encoder);
    }

    protected MessageEncoder adaptMessageEncoder(int index, String type, MessageEncoder encoder) {
        if (index < messageCodecAdapters.size()) {
            MessageEncoder adapter =
                    messageCodecAdapters.get(index).getMessageEncoder(type, encoder);
            return adaptMessageEncoder(index + 1, type, adapter);
        } else {
            return encoder;
        }
    }

    @Override
    public MessageDecoder getMessageDecoder(String type, MessageDecoder decoder) {
        return adaptMessageDecoder(0, type, decoder);
    }

    protected MessageDecoder adaptMessageDecoder(int index, String type, MessageDecoder decoder) {
        if (index < messageCodecAdapters.size()) {
            MessageDecoder adapter =
                    messageCodecAdapters.get(index).getMessageDecoder(type, decoder);
            return adaptMessageDecoder(index + 1, type, adapter);
        } else {
            return decoder;
        }
    }

    @Override
    public boolean support(String scope) {
        return true;
    }
}
