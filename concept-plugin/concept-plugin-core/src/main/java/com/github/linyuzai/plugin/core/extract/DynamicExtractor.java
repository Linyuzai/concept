package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.match.PluginText;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 动态插件提取器。
 * 可以根据自己的需求同时提取多个插件。
 */
public class DynamicExtractor implements PluginExtractor {

    /**
     * 方法参数对应的插件提取执行器缓存
     */
    protected final Map<Method, Map<Integer, Invoker>> methodInvokersMap = new LinkedHashMap<>();

    /**
     * 方法执行对象
     */
    @Getter
    protected final Object target;

    /**
     * 遍历所有的方法，
     * 如果方法上标注了注解 {@link OnPluginExtract}，
     * 尝试通过该方法的参数 {@link Type} 匹配对应的 {@link PluginExtractor}。
     *
     * @param target 方法执行对象
     */
    public DynamicExtractor(Object target) {
        this(target, getPluginMethod(target));
    }

    public DynamicExtractor(Object target, Method... methods) {
        this.target = target;
        for (Method method : methods) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Invoker invoker = getInvoker(parameters[i]);
                if (invoker == null) {
                    throw new PluginException("Can not invoke " + parameters[i]);
                }
                methodInvokersMap.computeIfAbsent(method, m ->
                        new LinkedHashMap<>()).put(i, invoker);
            }
        }
    }

    private static Method[] getPluginMethod(Object target) {
        Class<?> clazz = target.getClass();
        List<Method> annotated = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OnPluginExtract.class)) {
                    annotated.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        if (annotated.isEmpty()) {
            throw new PluginException("No method has @OnPluginExtract");
        }
        return annotated.toArray(new Method[0]);
    }

    /**
     * 通过 {@link Parameter} 获得一个执行器。
     * 如果标注了特殊的注解将会直接匹配，
     * {@link PluginText} 返回 {@link ContentExtractor} 对应的执行器。
     * 否则按照 {@link PluginContextExtractor} {@link PluginObjectExtractor}
     * {@link PropertiesExtractor} {@link ContentExtractor} 的顺序匹配执行器。
     *
     * @param parameter 方法参数 {@link Parameter}
     * @return 插件提取执行器
     */
    public Invoker getInvoker(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            Invoker invoker = getAnnotationInvoker(annotation, parameter);
            if (invoker != null) {
                return invoker;
            }
        }
        Invoker pluginContextInvoker = getPluginContextInvoker(parameter);
        if (pluginContextInvoker != null) {
            return pluginContextInvoker;
        }
        Invoker pluginObjectInvoker = getPluginObjectInvoker(parameter);
        if (pluginObjectInvoker != null) {
            return pluginObjectInvoker;
        }
        Invoker propertiesInvoker = getPropertiesInvoker(parameter);
        if (propertiesInvoker != null) {
            return propertiesInvoker;
        }
        Invoker contentInvoker = getContentInvoker(parameter, null);
        if (contentInvoker != null) {
            return contentInvoker;
        }
        return null;
    }

    /**
     * 根据明确指定的注解获得对应的执行器。
     * {@link PluginText} 返回 {@link ContentExtractor} 对应的执行器。
     *
     * @param annotation 注解
     * @param parameter  参数 {@link Parameter}
     * @return 插件提取执行器
     */
    public Invoker getAnnotationInvoker(Annotation annotation, Parameter parameter) {
        if (annotation.annotationType() == PluginText.class) {
            String charset = ((PluginText) annotation).charset();
            return getContentInvoker(parameter, charset.isEmpty() ? null : Charset.forName(charset));
        }
        return null;
    }

    /**
     * 尝试获得 {@link PluginContextExtractor} 对应的执行器。
     *
     * @param parameter 参数 {@link Parameter}
     * @return {@link PluginContextExtractor} 对应的执行器或 null
     */
    public Invoker getPluginContextInvoker(Parameter parameter) {
        try {
            return new PluginContextExtractor<PluginContext>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(PluginContext plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 尝试获得 {@link PluginObjectExtractor} 对应的执行器。
     *
     * @param parameter 参数 {@link Parameter}
     * @return {@link PluginObjectExtractor} 对应的执行器或 null
     */
    public Invoker getPluginObjectInvoker(Parameter parameter) {
        try {
            return new PluginObjectExtractor<Plugin>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Plugin plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 尝试获得 {@link PropertiesExtractor} 对应的执行器。
     *
     * @param parameter 参数 {@link Parameter}
     * @return {@link PropertiesExtractor} 对应的执行器或 null
     */
    public Invoker getPropertiesInvoker(Parameter parameter) {
        try {
            return new PropertiesExtractor<Void>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Void plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 尝试获得 {@link ContentExtractor} 对应的执行器。
     *
     * @param parameter 参数 {@link Parameter}
     * @return {@link ContentExtractor} 对应的执行器或 null
     */
    public Invoker getContentInvoker(Parameter parameter, Charset charset) {
        try {
            return new ContentExtractor<Void>(charset) {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Void plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * 提取插件。
     * 遍历所有的方法和参数，使用对应的执行器提取插件赋值给参数，
     * 调用对应的方法回调。
     * 如果方法参数没有匹配到任何插件，则不会回调。
     *
     * @param context 上下文 {@link PluginContext}
     */
    @SneakyThrows
    @Override
    public void extract(PluginContext context) {
        for (Map.Entry<Method, Map<Integer, Invoker>> entry : methodInvokersMap.entrySet()) {
            Method method = entry.getKey();
            Map<Integer, Invoker> matcherMap = entry.getValue();
            Object[] values = new Object[matcherMap.size()];
            boolean matched = false;
            for (Map.Entry<Integer, Invoker> methodEntry : matcherMap.entrySet()) {
                Integer index = methodEntry.getKey();
                Invoker invoker = methodEntry.getValue();
                Object invoked;
                try {
                    invoked = invoker.invoke(context);
                } catch (Throwable e) {
                    throw new PluginException("Invoke error on " + method.getName() + ", param " + index, e);
                }
                if (invoked == null) {
                    continue;
                }
                values[index] = invoked;
                matched = true;
            }
            if (matched) {
                try {
                    method.invoke(target, values);
                } catch (Throwable e) {
                    throw new PluginException("Invoke error on " + method.getName() + ", args " + Arrays.toString(values), e);
                }
                context.publish(new DynamicExtractedEvent(context, this, values, method, target));
            }
        }
    }

    /**
     * 获得依赖的解析器 {@link PluginResolver}。
     * 所有执行器中的匹配器依赖的解析器 {@link PluginResolver} 的集合。
     *
     * @return 所有依赖的解析器 {@link PluginResolver} 的类
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends PluginHandler>[] getDependencies() {
        return methodInvokersMap.values().stream()
                .flatMap(it -> it.values().stream())
                .flatMap(it -> Arrays.stream(it.getMatcher().getDependencies()))
                .distinct()
                .toArray(Class[]::new);
    }
}
