package com.github.linyuzai.connection.loadbalance.autoconfigure.subscribe.kafka;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.Message;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaMessageCodecAdapter extends AbstractMessageCodecAdapter {

    @Override
    public MessageDecoder getForwardMessageDecoder(MessageDecoder decoder) {
        return new KafkaMessageDecoder(decoder);
    }

    @Getter
    @RequiredArgsConstructor
    public static class KafkaMessageDecoder implements MessageDecoder {

        private final MessageDecoder decoder;

        @Override
        public Message decode(Object message, ConnectionLoadBalanceConcept concept) {
            if (message instanceof ConsumerRecord) {
                return decoder.decode(((ConsumerRecord<?, ?>) message).value(), concept);
            }
            return decoder.decode(message, concept);
        }
    }
}
