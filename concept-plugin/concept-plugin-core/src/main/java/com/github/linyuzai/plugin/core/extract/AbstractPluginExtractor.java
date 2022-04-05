package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * {@link PluginExtractor} 的抽象类
 *
 * @param <T> 插件类型
 */
public abstract class AbstractPluginExtractor<T> implements PluginExtractor {

    /**
     * 插件提取执行器
     */
    protected Invoker invoker;

    public Invoker getInvoker() {
        if (invoker == null) {
            invoker = getInvoker(getGenericType(), getAnnotations());
        }
        return invoker;
    }

    /**
     * 通过插件类型 {@link Type} 和注解获得执行器
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件提取执行器
     */
    public Invoker getInvoker(Type type, Annotation[] annotations) {
        PluginMatcher matcher = getMatcher(type, annotations);
        if (matcher == null) {
            throw new PluginException("Can not match " + type);
        }
        PluginConvertor convertor = getConvertor(type, annotations);
        PluginFormatter formatter = getFormatter(type, annotations);
        return new Invoker(matcher, convertor, formatter);
    }

    /**
     * 获得插件类型 {@link Type}，用于做插件匹配
     *
     * @return 插件类型的 {@link Type}
     */
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

    /**
     * 获得注解，用于做插件匹配
     *
     * @return 注解
     */
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    /**
     * 根据插件类型 {@link Type} 和注解获得 {@link PluginMatcher}
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件匹配器 {@link PluginMatcher}
     */
    public abstract PluginMatcher getMatcher(Type type, Annotation[] annotations);

    /**
     * 根据插件类型 {@link Type} 和注解获得 {@link PluginConvertor}
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件转换器 {@link PluginConvertor}
     */
    public PluginConvertor getConvertor(Type type, Annotation[] annotations) {
        return null;
    }

    /**
     * 根据插件类型 {@link Type} 和注解获得 {@link PluginFormatter}
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件格式器 {@link PluginFormatter}
     */
    public PluginFormatter getFormatter(Type type, Annotation[] annotations) {
        return null;
    }

    /**
     * 提取插件并发布 {@link PluginExtractedEvent} 事件
     *
     * @param context 上下文 {@link PluginContext}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void extract(PluginContext context) {
        //执行插件提取
        Object invoke = getInvoker().invoke(context);
        if (invoke == null) {
            return;
        }
        onExtract((T) invoke);
        context.publish(new PluginExtractedEvent(context, this, invoke));
    }

    /**
     * 插件提取回调
     *
     * @param plugin 插件对象
     */
    public abstract void onExtract(T plugin);

    /**
     * 依赖的解析器 {@link PluginResolver}
     *
     * @return 依赖的解析器 {@link PluginResolver} 的类
     */
    @Override
    public Class<? extends PluginResolver>[] dependencies() {
        return getInvoker().getMatcher().dependencies();
    }
}
