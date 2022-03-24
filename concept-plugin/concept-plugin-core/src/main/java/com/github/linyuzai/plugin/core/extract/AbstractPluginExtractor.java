package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
public abstract class AbstractPluginExtractor<T> implements PluginExtractor {

    protected final Invoker invoker;

    public AbstractPluginExtractor() {
        invoker = getInvoker(getGenericType(), new Annotation[0]);
    }

    public Invoker getInvoker(Type type, Annotation[] annotations) {
        PluginMatcher matcher = getMatcher(type, annotations);
        if (matcher == null) {
            throw new PluginException("Can not match " + type);
        }
        PluginConvertor convertor = getConvertor(type, annotations);
        PluginFormatter formatter = getFormatter(type, annotations);
        return new Invoker(matcher, convertor, formatter);
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

    public abstract PluginMatcher getMatcher(Type type, Annotation[] annotations);

    public PluginConvertor getConvertor(Type type, Annotation[] annotations) {
        return null;
    }

    public PluginFormatter getFormatter(Type type, Annotation[] annotations) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void extract(PluginContext context) {
        Object invoke = invoker.invoke(context);
        if (invoke == null) {
            return;
        }
        onExtract((T) invoke);
    }

    public abstract void onExtract(T plugin);

    @Override
    public Class<? extends PluginResolver>[] dependencies() {
        return invoker.getMatcher().dependencies();
    }
}
