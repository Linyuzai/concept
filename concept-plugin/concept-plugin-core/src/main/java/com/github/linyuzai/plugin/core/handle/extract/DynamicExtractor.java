package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginText;
import com.github.linyuzai.plugin.core.handle.resolve.PluginResolver;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 动态插件提取器。
 * 可以根据自己的需求同时提取多个插件。
 */
public class DynamicExtractor implements MethodPluginExtractor {

    /**
     * 方法参数对应的插件提取执行器缓存
     */
    protected final Map<Method, Map<Integer, Invoker>> methodInvokersMap = new LinkedHashMap<>();

    /**
     * 方法执行对象
     */
    @Getter
    protected final Object target;

    @Getter
    protected final Method[] methods;

    @Getter
    protected final List<InvokerFactory> invokerFactories = new CopyOnWriteArrayList<>();

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
        this.methods = methods;
    }

    protected static Method[] getPluginMethod(Object target) {
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

    public void useDefaultInvokerFactories() {
        addInvokerFactory(new PluginObjectExtractor.InvokerFactory());
        addInvokerFactory(new PluginContextExtractor.InvokerFactory());
        addInvokerFactory(new PropertiesExtractor.InvokerFactory());
        addInvokerFactory(new ContentExtractor.InvokerFactory());
    }

    @Override
    public void addInvokerFactory(InvokerFactory factory) {
        this.invokerFactories.add(factory);
    }

    @Override
    public void removeInvokerFactory(InvokerFactory factory) {
        this.invokerFactories.remove(factory);
    }

    @Override
    public synchronized void prepareInvokers() {
        methodInvokersMap.clear();
        for (Method method : methods) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Invoker invoker = getInvoker(method, parameters[i]);
                if (invoker == null) {
                    throw new PluginException("Can not invoke " + parameters[i]);
                }
                methodInvokersMap.computeIfAbsent(method, m ->
                        new LinkedHashMap<>()).put(i, invoker);
            }
        }
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
    public Invoker getInvoker(Method method, Parameter parameter) {
        for (InvokerFactory invokerFactory : invokerFactories) {
            Invoker invoker = invokerFactory.create(method, parameter);
            if (invoker != null) {
                return invoker;
            }
        }
        return null;
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
            Map<Integer, Invoker> invokerMap = entry.getValue();
            Object[] values = new Object[invokerMap.size()];
            boolean matched = false;
            for (Map.Entry<Integer, Invoker> invokerEntry : invokerMap.entrySet()) {
                Integer index = invokerEntry.getKey();
                Invoker invoker = invokerEntry.getValue();
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
                .flatMap(it -> Arrays.stream(it.getDependencies()))
                .distinct()
                .toArray(Class[]::new);
    }
}
