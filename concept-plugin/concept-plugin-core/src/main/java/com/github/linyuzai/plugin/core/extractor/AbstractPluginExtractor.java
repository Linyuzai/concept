package com.github.linyuzai.plugin.core.extractor;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import lombok.AllArgsConstructor;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@AllArgsConstructor
public abstract class AbstractPluginExtractor<T> implements PluginExtractor {

    private final PluginMatcher matcher;

    public AbstractPluginExtractor() {
        Type type = getGenericType();
        this.matcher = bind(type);
        if (this.matcher == null) {
            Class<?> clazz = getClass();
            int modifiers = clazz.getModifiers();
            Type classType;
            if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
                classType = getClass().getGenericSuperclass();
            } else {
                classType = getClass();
            }
            throw new PluginException("Can not bind " + type + " to " + classType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void extract(PluginContext context) {
        if (matcher.isMatched(context)) {
            onExtract((T) matcher.getMatched(context));
        }
    }

    public abstract PluginMatcher bind(Type type);

    public Type getGenericType() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types.length == 1) {
                return types[0];
            }
        }
        throw new PluginException("U may need to try override this method");
    }

    public abstract void onExtract(T plugin);
}
