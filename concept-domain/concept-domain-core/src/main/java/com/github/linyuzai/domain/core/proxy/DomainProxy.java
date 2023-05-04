package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainObject;
import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 领域代理
 */
public interface DomainProxy extends InvocationHandler {

    Map<Method, MethodHandle> DEFAULT_METHOD_HANDLES = new ConcurrentHashMap<>();

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
            MethodHandle handle = DEFAULT_METHOD_HANDLES.computeIfAbsent(method, m -> getMethodHandle(method, proxy));
            return handle.invokeWithArguments(args);
        } else {
            return doInvoke(proxy, method, args);
        }
    }

    default Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this, args);
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
}
