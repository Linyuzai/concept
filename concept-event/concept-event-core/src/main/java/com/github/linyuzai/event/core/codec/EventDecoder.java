package com.github.linyuzai.event.core.codec;

import java.lang.reflect.Type;

public interface EventDecoder {

    Object decode(Object event, Type type);
}
