package com.github.linyuzai.plugin.core.type;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 嵌套类型的默认实现
 */
@Getter
@Setter
public class DefaultNestedType implements NestedType {

    private Type type;

    private Class<?> cls;

    private NestedType parent;

    private final List<NestedType> children = new ArrayList<>();

    public DefaultNestedType(Type type) {
        this(type, null);
    }

    public DefaultNestedType(Type type, NestedType parent) {
        this.type = type;
        this.parent = parent;
        ReflectionUtils.resolve(type, (cls, types) -> {
            DefaultNestedType.this.cls = cls;
            for (Type t : types) {
                children.add(new DefaultNestedType(t, this));
            }
        });
    }

    @Override
    public Type toType() {
        return type;
    }

    @Override
    public Class<?> toClass() {
        return cls;
    }
}
