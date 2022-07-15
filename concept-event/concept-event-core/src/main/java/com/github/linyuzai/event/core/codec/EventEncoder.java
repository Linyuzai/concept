package com.github.linyuzai.event.core.codec;

public interface EventEncoder {

    Object encode(Object event);
}
