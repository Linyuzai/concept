package com.github.linyuzai.plugin.core.type;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 嵌套类型的默认实现
 */
@Getter
public class DefaultNestedType implements NestedType {

    protected Type type;

    protected Class<?> cls;

    protected NestedType parent;

    protected List<NestedType> children = new ArrayList<>();

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
