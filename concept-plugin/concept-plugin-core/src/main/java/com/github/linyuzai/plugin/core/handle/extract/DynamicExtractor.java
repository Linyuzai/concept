package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.util.SyncSupport;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 动态插件提取器
 */
public class DynamicExtractor extends SyncSupport implements MethodPluginExtractor {

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
    protected final List<InvokerFactory> invokerFactories = new ArrayList<>();

    public DynamicExtractor(Object target) {
        this(target, getPluginMethod(target));
    }

    public DynamicExtractor(Object target, Method... methods) {
        this.target = target;
        this.methods = methods;
    }

    /**
     * 获得所有标记了 {@link OnPluginExtract} 的方法
     */
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

    /**
     * 使用默认的提取执行器工厂
     */
    public void useDefaultInvokerFactories() {
        addInvokerFactory(new PluginObjectExtractor.InvokerFactory());
        addInvokerFactory(new PluginContextExtractor.InvokerFactory());
        addInvokerFactory(new PropertiesExtractor.InvokerFactory());
        addInvokerFactory(new ContentExtractor.InvokerFactory());
    }

    @Override
    public void addInvokerFactory(InvokerFactory factory) {
        syncWrite(() -> this.invokerFactories.add(factory));
    }

    @Override
    public void removeInvokerFactory(InvokerFactory factory) {
        syncWrite(() -> this.invokerFactories.remove(factory));
    }

    @Override
    public void initialize() {
        syncWrite(() -> {
            methodInvokersMap.clear();
            for (Method method : methods) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                //获得方法参数
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    //根据参数获得提取执行器
                    Invoker invoker = createInvoker(method, parameters[i]);
                    if (invoker == null) {
                        throw new PluginException("Can not invoke " + parameters[i]);
                    }
                    methodInvokersMap.computeIfAbsent(method, m ->
                            new LinkedHashMap<>()).put(i, invoker);
                }
            }
        });
    }

    /**
     * 通过提取执行器工厂创建
     */
    protected Invoker createInvoker(Method method, Parameter parameter) {
        for (InvokerFactory invokerFactory : invokerFactories) {
            Invoker invoker = invokerFactory.create(method, parameter);
            if (invoker != null) {
                return invoker;
            }
        }
        return null;
    }

    /**
     * 提取插件
     * <p>
     * 遍历所有的方法和参数，使用对应的执行器提取插件赋值给参数
     * <p>
     * 调用对应的方法回调
     * <p>
     * 如果方法参数没有匹配到任何插件，则不会回调
     */
    @SneakyThrows
    @Override
    public void extract(PluginContext context) {
        syncRead(() -> {
            for (Map.Entry<Method, Map<Integer, Invoker>> entry : methodInvokersMap.entrySet()) {
                Method method = entry.getKey();
                //方法参数对应的提取执行器
                Map<Integer, Invoker> invokerMap = entry.getValue();
                //方法入参
                Object[] values = new Object[invokerMap.size()];
                //是否匹配到插件
                boolean matched = false;
                for (Map.Entry<Integer, Invoker> invokerEntry : invokerMap.entrySet()) {
                    //方法参数下标
                    Integer index = invokerEntry.getKey();
                    Invoker invoker = invokerEntry.getValue();
                    Object invoked;
                    try {
                        //获得参数值
                        invoked = invoker.invoke(context);
                    } catch (Throwable e) {
                        throw new PluginException("Invoke error on " + method.getName() + ", param " + index, e);
                    }
                    if (invoked == null) {
                        continue;
                    }
                    //设置参数值
                    values[index] = invoked;
                    //Plugin PluginContext 不会被视为匹配
                    if (isExtractable(invoked)) {
                        matched = true;
                    }
                }
                //如果匹配到任意值则回调方法
                if (matched) {
                    try {
                        method.invoke(target, values);
                    } catch (Throwable e) {
                        throw new PluginException("Invoke error on " + method.getName() + ", args " + Arrays.toString(values), e);
                    }
                    DynamicExtractedEvent event = new DynamicExtractedEvent(context, this, values, method, target);
                    context.getConcept().getEventPublisher().publish(event);
                }
            }
        });
    }

    /**
     * 是否算是可提取的数据
     */
    protected boolean isExtractable(Object object) {
        return !(object instanceof Plugin) && !(object instanceof PluginContext);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends PluginHandler>[] getDependencies() {
        return syncRead(() -> methodInvokersMap.values().stream()
                .flatMap(it -> it.values().stream())
                .flatMap(it -> Arrays.stream(it.getDependencies()))
                .distinct()
                .toArray(Class[]::new));
    }
}
