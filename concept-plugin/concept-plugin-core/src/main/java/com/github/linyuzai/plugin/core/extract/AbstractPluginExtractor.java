package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
@AllArgsConstructor
public abstract class AbstractPluginExtractor<T> implements PluginExtractor {

    protected PluginMatcher matcher;

    public AbstractPluginExtractor() {
        match(getGenericType());
    }

    public void match(Type type) {
        this.matcher = getMatcher(type);
        if (this.matcher == null) {
            throw new PluginException("Can not match " + type);
        }
    }

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

    public abstract PluginMatcher getMatcher(Type type);

    @SuppressWarnings("unchecked")
    @Override
    public void extract(PluginContext context) {
        Object match = matcher.match(context);
        if (match == null) {
            return;
        }
        onExtract((T) match);
    }

    public abstract void onExtract(T plugin);

    @Override
    public Class<? extends PluginResolver>[] dependencies() {
        return matcher.dependencies();
    }
}
