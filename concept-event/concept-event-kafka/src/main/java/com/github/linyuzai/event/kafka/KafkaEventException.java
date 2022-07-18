package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.exception.EventException;

public class KafkaEventException extends EventException {

    public KafkaEventException(String message) {
        super(message);
    }

    public KafkaEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
