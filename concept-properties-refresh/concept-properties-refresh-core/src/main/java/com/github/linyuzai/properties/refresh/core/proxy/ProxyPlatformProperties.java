/*
package com.github.linyuzai.properties.refresh.core.proxy;

import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

*/
/**
 * 代理配置属性抽象实现
 * 不支持自动刷新！
 *
 * @see RefreshableProxyPlatformProperties
 *//*

public abstract class ProxyPlatformProperties implements LifecyclePlatformProperties {

    */
/**
     * 需要代理的方法（获得数据的字段方法而不是回调）
     *//*

    private final Map<Method, Metadata> proxyMetadataMap = new ConcurrentHashMap<>();

    */
/**
     * 默认方法句柄
     *//*

    private final Map<Method, MethodHandle> defaultMethodMap = new ConcurrentHashMap<>();

    @SneakyThrows
    @Override
    public void register(Object object) {
        List<Field> fields = ReflectUtils.getFields(object);
        for (Field field : fields) {
            Class<?> clazz = field.getType();
            if (clazz.isInterface() && clazz.isAnnotationPresent(PropertiesModel.class)) {
                //必须是接口，并且接口上标注了注解
                List<Method> methods = ReflectUtils.getMethods(clazz);
                //生成代理
                Object proxy = proxy(clazz);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(object, proxy);
                //处理方法
                registerMethods(proxy, methods);
            }
        }
    }

    */
/**
     * 初始化方法返回值类型和匹配的key
     * 初始化默认方法的句柄
     *
     * @param proxy   代理对象
     * @param methods 方法列表
     * @throws Throwable 异常
     *//*

    public void registerMethods(Object proxy, List<Method> methods) throws Throwable {
        for (Method method : methods) {
            handleMethodIfDefault(method, proxy);
            RefreshableProperties annotation = method.getAnnotation(RefreshableProperties.class);
            if (annotation == null) {
                continue;
            }
            int parameterCount = method.getParameterCount();
            //方法参数必须是0
            if (parameterCount == 0) {
                Metadata metadata = new Metadata(annotation.value(), method.getGenericReturnType());
                addProxyMetadata(method, metadata);
            } else {
                throw new PlatformPropertiesException("Parameter count of method must be 0: " + method);
            }
        }
    }

    */
/**
     * 获得代理值
     *
     * @param metadata 类型及匹配的key
     * @param method   方法
     * @return 代理属性值
     *//*

    public Object getProxyValue(Metadata metadata, Method method) {
        return AbstractPropertiesRefresher.getValue(metadata, this);
    }

    public void addProxyMetadata(Method method, Metadata metadata) {
        proxyMetadataMap.put(method, metadata);
    }

    */
/**
     * 获得方法句柄
     *
     * @param method 方法
     * @param proxy  代理对象
     * @return 默认方法句柄
     *//*

    @SneakyThrows
    public MethodHandle getMethodHandle(Method method, Object proxy) {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        return constructor.
                newInstance(declaringClass, MethodHandles.Lookup.PRIVATE).
                unreflectSpecial(method, declaringClass).
                bindTo(proxy);
    }

    public void handleMethodIfDefault(Method method, Object proxy) {
        if (method.isDefault()) {
            defaultMethodMap.put(method, getMethodHandle(method, proxy));
        }
    }

    */
/**
     * 代理
     *
     * @param clazz 需要代理的属性接口
     * @return 代理对象
     *//*

    public Object proxy(Class<?> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                (proxy, method, args) -> {
                    //获得方法的返回值类型和匹配的key
                    Metadata metadata = proxyMetadataMap.get(method);
                    if (metadata == null) {
                        MethodHandle methodHandle = defaultMethodMap.get(method);
                        if (methodHandle == null) {
                            throw new PlatformPropertiesException("Method is not proxy and no default impl: " + method);
                        } else {
                            return methodHandle.invokeWithArguments(args);
                        }
                    } else {
                        //获得代理值
                        Object proxyValue = getProxyValue(metadata, method);
                        if (proxyValue == null) {
                            //如果为null则尝试获得默认返回值
                            MethodHandle methodHandle = defaultMethodMap.get(method);
                            if (methodHandle == null) {
                                return null;
                            } else {
                                return methodHandle.invokeWithArguments(args);
                            }
                        } else {
                            return proxyValue;
                        }
                    }
                });
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {
        //清空缓存
        proxyMetadataMap.clear();
        defaultMethodMap.clear();
    }
}
*/
