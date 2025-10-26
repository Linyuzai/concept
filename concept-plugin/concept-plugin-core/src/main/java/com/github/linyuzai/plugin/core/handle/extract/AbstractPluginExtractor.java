package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.format.*;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.DefaultNestedTypeFactory;
import com.github.linyuzai.plugin.core.type.NestedType;
import com.github.linyuzai.plugin.core.type.NestedTypeFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 插件提取器抽象类
 */
public abstract class AbstractPluginExtractor<T> implements PluginExtractor {

    @Getter
    @Setter
    protected NestedTypeFactory nestedTypeFactory = DefaultNestedTypeFactory.getInstance();

    /**
     * 插件提取执行器
     */
    protected volatile Invoker invoker;

    /**
     * 获得插件提取执行器
     */
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

    /**
     * 创建插件提取执行器
     */
    protected Invoker createInvoker() {
        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            //匹配方法名
            if ("onExtract".equals(method.getName()) && !method.isBridge()) {
                Parameter[] parameters = method.getParameters();
                //参数是2个
                if (parameters.length != 2) {
                    continue;
                }
                //第二个参数是PluginContext
                if (!PluginContext.class.isAssignableFrom(parameters[1].getType())) {
                    continue;
                }
                return createInvoker(method, parameters[0]);
            }
        }
        return null;
    }

    /**
     * 根据 方法和参数 获得插件提取执行器
     */
    public Invoker createInvoker(Method method, Parameter parameter) {
        Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<>();
        putAnnotations(method.getAnnotations(), annotationMap);
        putAnnotations(parameter.getAnnotations(), annotationMap);
        Annotation[] annotations = annotationMap.values().toArray(new Annotation[0]);
        return createInvoker(parameter.getParameterizedType(), annotations);
    }

    /**
     * 覆盖注解
     */
    protected void putAnnotations(Annotation[] annotations,
                                  Map<Class<? extends Annotation>, Annotation> annotationMap) {
        for (Annotation annotation : annotations) {
            annotationMap.put(annotation.annotationType(), annotation);
        }
    }

    /**
     * 通过插件类型 {@link Type} 和注解获得执行器
     */
    public Invoker createInvoker(Type type, Annotation[] annotations) {
        //获得嵌套类型
        NestedType nestedType = nestedTypeFactory.create(type);
        return createInvoker(nestedType, annotations);
    }

    /**
     * 通过插件类型 {@link NestedType} 和注解获得执行器
     */
    public Invoker createInvoker(NestedType nestedType, Annotation[] annotations) {
        if (nestedType == null) {
            throw new PluginException("Nested type is null");
        }
        Type type = nestedType.toType();
        if (nestedType.toClass() == null) {
            throw new PluginException("Can not resolve type: " + type);
        }
        //获得格式化器
        PluginFormatter formatter = getFormatter(nestedType, annotations);
        //格式化器对嵌套类型有修改
        if (formatter instanceof NestedTypeFormatter) {
            //使用修改后的嵌套类型
            nestedType = ((NestedTypeFormatter) formatter).getNestedType();
            //获得真实的格式化器
            formatter = ((NestedTypeFormatter) formatter).formatter;
        }
        //获得匹配器
        PluginMatcher matcher = getMatcher(nestedType, annotations);
        if (matcher == null) {
            throw new PluginException("Can not match type: " + type);
        }
        //获得转换器
        PluginConvertor convertor = getConvertor(nestedType, annotations);
        return new InvokerImpl(matcher, convertor, formatter);
    }

    /**
     * 根据嵌套类型和注解获得匹配器
     */
    public abstract PluginMatcher getMatcher(NestedType type, Annotation[] annotations);

    /**
     * 根据嵌套类型和注解获得转换器
     */
    public PluginConvertor getConvertor(NestedType type, Annotation[] annotations) {
        return null;
    }

    /**
     * 根据嵌套类型和注解获得格式化器
     * <p>
     * 如果是容器类型则返回子类型作为嵌套类型
     */
    public PluginFormatter getFormatter(NestedType type, Annotation[] annotations) {
        Class<?> cls = type.toClass();
        if (Map.class.isAssignableFrom(cls)) {
            return new NestedTypeFormatter(new MapFormatter(cls), getChild(type, 1));
        } else if (List.class.isAssignableFrom(cls)) {
            return new NestedTypeFormatter(new ListFormatter(cls), getChild(type, 0));
        } else if (Set.class.isAssignableFrom(cls)) {
            return new NestedTypeFormatter(new SetFormatter(cls), getChild(type, 0));
        } else if (Collection.class.isAssignableFrom(cls)) {
            return new NestedTypeFormatter(new ListFormatter(cls), getChild(type, 0));
        } else if (cls.isArray()) {
            return new NestedTypeFormatter(new ArrayFormatter(cls), type.getChildren().get(0));
        } else {
            return new ObjectFormatter();
        }
    }

    /**
     * 如果存在子类型则返回子类型，否则返回Object类型
     */
    protected NestedType getChild(NestedType type, int index) {
        List<NestedType> children = type.getChildren();
        if (children.size() > index) {
            return children.get(index);
        }
        return getNestedTypeFactory().create(Object.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void extract(PluginContext context) {
        //执行插件提取
        Object invoke = getInvoker().invoke(context);
        if (invoke == null) {
            //如果没有提取到则不触发回调
            return;
        }
        onExtract((T) invoke, context);
        PluginExtractedEvent event = new PluginExtractedEvent(context, this, invoke);
        context.getConcept().getEventPublisher().publish(event);
    }

    /**
     * 插件提取回调
     */
    public abstract void onExtract(T plugin, PluginContext context);

    /**
     * 获得依赖的解析器类型
     */
    @Override
    public Class<? extends PluginHandler>[] getDependencies() {
        return getInvoker().getDependencies();
    }

    /**
     * 携带修改后的嵌套类型
     */
    @Getter
    @RequiredArgsConstructor
    public static class NestedTypeFormatter implements PluginFormatter {

        private final PluginFormatter formatter;

        private final NestedType nestedType;

        @Override
        public Object format(Object source, PluginContext context) {
            return formatter.format(source, context);
        }
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
         * 执行插件提取，包括匹配，转换，格式化三个步骤
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

    /**
     * 插件提取执行器工厂抽象类
     */
    public static abstract class InvokerFactory implements MethodPluginExtractor.InvokerFactory {

        @Override
        public Invoker create(Method method, Parameter parameter) {
            try {
                return createExtractor().createInvoker(method, parameter);
            } catch (Throwable e) {
                return null;
            }
        }

        protected abstract AbstractPluginExtractor<?> createExtractor();
    }
}
