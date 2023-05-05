package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainContext;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.lambda.LambdaFunction;
import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 领域代理
 */
public interface DomainProxy extends InvocationHandler {

    Map<Method, MethodHandle> DEFAULT_METHOD_HANDLES = new ConcurrentHashMap<>();

    Map<Method, Field> PROXY_FIELDS = new ConcurrentHashMap<>();

    Map<Method, Method> PROXY_METHODS = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    default <I extends DomainObject> I create(Class<I> cls) {
        if (cls.isInterface()) {
            return (I) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, this);
        }
        throw new IllegalArgumentException("Class must be interface");
    }

    @Override
    default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            MethodHandle handle = DEFAULT_METHOD_HANDLES.computeIfAbsent(method, m ->
                    getMethodHandle(method, proxy));
            return handle.invokeWithArguments(args);
        } else {
            DomainProxyField dpf = method.getAnnotation(DomainProxyField.class);
            if (dpf != null) {
                Field proxyField = PROXY_FIELDS.computeIfAbsent(method, m -> {
                    String name = getProxyAnnotationValue(dpf.value(), method.getName());
                    return getProxyField(name, getProxied());
                });
                return proxyField.get(getProxied());
            }
            DomainProxyMethod dpm = method.getAnnotation(DomainProxyMethod.class);
            if (dpm != null) {
                Method proxyMethod = PROXY_METHODS.computeIfAbsent(method, m -> {
                    String name = getProxyAnnotationValue(dpm.value(), method.getName());
                    return getProxyMethod(name, method.getParameterTypes(), getProxied());
                });
                return proxyMethod.invoke(getProxied(), args);
            }
            return doInvoke(proxy, method, args);
        }
    }

    default Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(getProxied(), args);
    }

    default Object getProxied() {
        return this;
    }

    static String getProxyAnnotationValue(String value, String method) {
        return value.isEmpty() ? method : value;
    }

    @SneakyThrows
    static MethodHandle getMethodHandle(Method method, Object proxy) {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        return constructor.
                newInstance(declaringClass, MethodHandles.Lookup.PRIVATE).
                unreflectSpecial(method, declaringClass).
                bindTo(proxy);
    }

    @SneakyThrows
    static Field getProxyField(String name, Object o) {
        String fieldName = new LambdaFunction.Name(name).convertGetMethodToField().lowercaseFirst().getValue();
        Field field = getField(o.getClass(), fieldName);
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        field.setAccessible(true);
        return field;
    }

    @SneakyThrows
    static Method getProxyMethod(String name, Class<?>[] parameterTypes, Object o) {
        Method method = getMethod(o.getClass(), name, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException(name);
        }
        method.setAccessible(true);
        return method;
    }

    @SneakyThrows
    static Field getField(Class<?> clazz, String name) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return getField(clazz.getSuperclass(), name);
        }
    }

    @SneakyThrows
    static Method getMethod(Class<?> clazz, String name, Class<?>[] parameterTypes) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return getMethod(clazz.getSuperclass(), name, parameterTypes);
        }
    }

    interface ContextAccess {

        DomainContext getContext();
    }

    interface ConditionsAccess {

        Conditions getConditions();
    }

    interface RepositoryAccess<T extends DomainObject> {

        DomainRepository<T, ?> getRepository();
    }

    interface CollectionAccess<T extends DomainObject> {

        DomainCollection<T> getCollection();
    }

    interface ExtraAccess<T> {

        T getExtra();

        void setExtra(T custom);
    }
}
