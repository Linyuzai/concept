package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.resolve.PluginResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link PluginExtractor} 的抽象类
 *
 * @param <T> 插件类型
 */
public abstract class AbstractPluginExtractor<T> implements PluginExtractor {

    /**
     * 插件提取执行器
     */
    protected volatile Invoker invoker;

    public Invoker getInvoker() {
        if (invoker == null) {
            synchronized (this) {
                if (invoker == null) {
                    invoker = createInvoker();
                }
            }
        }
        return invoker;
    }

    protected Invoker createInvoker() {
        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if ("onExtract".equals(method.getName()) && !method.isBridge()) {
                Parameter[] parameters = method.getParameters();
                if (parameters.length != 2) {
                    continue;
                }
                if (!PluginContext.class.isAssignableFrom(parameters[1].getType())) {
                    continue;
                }
                return createInvoker(method, parameters[0]);
            }
        }
        return null;
    }

    public Invoker createInvoker(Method method, Parameter parameter) {
        Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<>();
        putAnnotations(method.getAnnotations(), annotationMap);
        putAnnotations(parameter.getAnnotations(), annotationMap);
        Annotation[] annotations = annotationMap.values().toArray(new Annotation[0]);
        return createInvoker(parameter.getParameterizedType(), annotations);
    }

    protected void putAnnotations(Annotation[] annotations,
                                  Map<Class<? extends Annotation>, Annotation> annotationMap) {
        for (Annotation annotation : annotations) {
            annotationMap.put(annotation.annotationType(), annotation);
        }
    }

    /**
     * 通过插件类型 {@link Type} 和注解获得执行器
     *
     * @param type        插件类型 {@link Type}
     * @param annotations 注解
     * @return 插件提取执行器
     */
    public Invoker createInvoker(Type type, Annotation[] annotations) {
        PluginMatcher matcher = getMatcher(type, annotations);
        if (matcher == null) {
            throw new PluginException("Can not match " + type);
        }
        PluginConvertor convertor = getConvertor(type, annotations);
        PluginFormatter formatter = getFormatter(type, annotations);
        return new InvokerImpl(matcher, convertor, formatter);
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
        onExtract((T) invoke, context);
        context.publish(new PluginExtractedEvent(context, this, invoke));
    }

    /**
     * 插件提取回调
     *
     * @param plugin 插件对象
     */
    public abstract void onExtract(T plugin, PluginContext context);

    /**
     * 依赖的解析器 {@link PluginResolver}
     *
     * @return 依赖的解析器 {@link PluginResolver} 的类
     */
    @Override
    public Class<? extends PluginHandler>[] getDependencies() {
        return getInvoker().getDependencies();
    }

    @Getter
    @RequiredArgsConstructor
    public static class InvokerImpl implements Invoker {

        /**
         * 插件匹配器
         */
        private final PluginMatcher matcher;

        /**
         * 插件转换器
         */
        private final PluginConvertor convertor;

        /**
         * 插件格式器
         */
        private final PluginFormatter formatter;

        /**
         * 执行插件提取。
         * 包括匹配，转换，格式化三个步骤。
         *
         * @param context 上下文 {@link PluginContext}
         * @return 插件对象
         */
        @Override
        public Object invoke(PluginContext context) {
            //匹配插件
            Object matched = matcher.match(context);
            if (matched == null) {
                return null;
            }
            //转换插件
            Object converted = convertor == null ? matched : convertor.convert(matched, context);
            if (converted == null) {
                return null;
            }
            //格式化插件
            return formatter == null ? converted : formatter.format(converted, context);
        }

        @Override
        public Class<? extends PluginHandler>[] getDependencies() {
            return matcher.getDependencies();
        }
    }
}
