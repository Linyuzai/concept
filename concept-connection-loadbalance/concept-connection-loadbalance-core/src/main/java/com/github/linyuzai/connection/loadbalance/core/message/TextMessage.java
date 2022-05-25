package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.NoArgsConstructor;

/**
 * 文本消息
 */
@NoArgsConstructor
public class TextMessage extends AbstractMessage<String> {

    public TextMessage(String payload) {
        setPayload(payload);
    }
}
