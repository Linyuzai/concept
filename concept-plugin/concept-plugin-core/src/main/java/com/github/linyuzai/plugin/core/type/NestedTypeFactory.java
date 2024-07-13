package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;

/**
 * 嵌套类型工厂
 */
public interface NestedTypeFactory {

    /**
     * 创建嵌套类型
     */
    NestedType create(Type type);
}
