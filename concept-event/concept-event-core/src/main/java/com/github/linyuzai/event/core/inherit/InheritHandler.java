package com.github.linyuzai.event.core.inherit;

/**
 * 继承处理器
 *
 * @param <T> 继承类型
 */
public interface InheritHandler<T extends Inheritable> {

    void inherit(T inheritable);
}
