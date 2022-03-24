package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
public abstract class AbstractPluginExtractor<T> implements PluginExtractor {

    protected PluginMatcher matcher;

    @Setter
    protected PluginConvertor convertor;

    public AbstractPluginExtractor() {
        match(getGenericType(), new Annotation[0]);
    }

    public void match(Type type, Annotation[] annotations) {
        this.matcher = getMatcher(type, annotations);
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

    public abstract PluginMatcher getMatcher(Type type, Annotation[] annotations);

    @SuppressWarnings("unchecked")
    @Override
    public void extract(PluginContext context) {
        Object match = matcher.match(context);
        if (match == null) {
            return;
        }
        Object convert = convertor.convert(match);
        if (convert == null) {
            return;
        }
        onExtract((T) convert);
    }

    public abstract void onExtract(T plugin);

    @Override
    public Class<? extends PluginResolver>[] dependencies() {
        return matcher.dependencies();
    }
}
