package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.util.TypeMetadata;

import java.lang.reflect.Type;

public abstract class AbstractPluginMatcher<T> implements PluginMatcher {

    private TypeMetadata metadata;

    @Override
    public void match(PluginContext context) {
        if (tryMatch(context)) {
            onMatched(getMatched(context));
        }
    }

    @Override
    public boolean match(Type type) {
        metadata = TypeMetadata.from(type);
        if (metadata == null) {
            return false;
        }
        return match(metadata, type);
    }

    public abstract boolean match(TypeMetadata metadata, Type type);

    public T getMatched(PluginContext context) {
        return context.get(this);
    }

    public abstract boolean tryMatch(PluginContext context);

    public void onMatched(T plugin) {

    }
}
