package com.github.linyuzai.event.core.codec;

public interface EventDecoder {

    Object decode(Object event);
}
