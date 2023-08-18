package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.lambda.LambdaFunction;
import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 领域代理
 */
public interface DomainProxy extends DomainProperties, InvocationHandler {

    Map<Method, MethodHandle> DEFAULT_METHOD_HANDLES = new ConcurrentHashMap<>();

    @Deprecated
    Map<Method, Field> PROXY_FIELDS = new ConcurrentHashMap<>();

    @Deprecated
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
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass == DomainObject.class || declaringClass == Identifiable.class || isAccess(declaringClass)) {
            return method.invoke(this, args);
        }
        /*DomainProxyField dpf = method.getAnnotation(DomainProxyField.class);
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
        }*/
        if (method.isDefault()) {
            boolean useCache = method.isAnnotationPresent(DomainProxyCache.class);
            Map<Method, Object> cache = withPropertyKey(DomainProxyCache.class, LinkedHashMap::new);
            if (useCache && cache.containsKey(method)) {
                return cache.get(method);
            }
            MethodHandle handle = DEFAULT_METHOD_HANDLES.computeIfAbsent(method, m -> getMethodHandle(method));
            MethodHandle bind = handle.bindTo(proxy);
            Object value = bind.invokeWithArguments(args);
            if (useCache) {
                cache.put(method, value);
            }
            return value;
        } else {
            return doInvoke(getProxied(), method, args);
        }
    }

    default Object doInvoke(Object proxied, Method method, Object[] args) throws Throwable {
        return method.invoke(proxied, args);
    }

    default boolean isAccess(Class<?> clazz) {
        return clazz == ContextAccess.class ||
                clazz == ConditionsAccess.class ||
                clazz == RepositoryAccess.class ||
                clazz == ExtraAccess.class;
    }

    default Object getProxied() {
        return this;
    }

    static String getProxyAnnotationValue(String value, String method) {
        return value.isEmpty() ? method : value;
    }

    @SneakyThrows
    static MethodHandle getMethodHandle(Method method) {
        //MethodHandles.lookup().unreflectSpecial()
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        return constructor.newInstance(declaringClass, MethodHandles.Lookup.PUBLIC | MethodHandles.Lookup.PRIVATE).unreflectSpecial(method, declaringClass);
    }

    @Deprecated
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

    @Deprecated
    @SneakyThrows
    static Method getProxyMethod(String name, Class<?>[] parameterTypes, Object o) {
        Method method = getMethod(o.getClass(), name, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException(name);
        }
        method.setAccessible(true);
        return method;
    }

    @Deprecated
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

    @Deprecated
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

    static boolean hasAccess(Class<? extends DomainObject> type) {
        if (!type.isInterface()) {
            return false;
        }
        if (ContextAccess.class.isAssignableFrom(type) ||
                ConditionsAccess.class.isAssignableFrom(type) ||
                RepositoryAccess.class.isAssignableFrom(type) ||
                ExtraAccess.class.isAssignableFrom(type)) {
            return true;
        }
        for (Method method : type.getMethods()) {
            if (method.isAnnotationPresent(DomainProxyCache.class)) {
                return true;
            }
        }
        return false;
    }

    interface ContextAccess {

        default DomainContext getContext() {
            return null;
        }
    }

    interface ConditionsAccess {

        default Conditions getConditions() {
            return null;
        }
    }

    interface RepositoryAccess<T extends DomainObject> {

        default DomainRepository<T, ?> getRepository() {
            return null;
        }
    }

    interface ExtraAccess<T> extends DomainProperties {

        default T getExtra() {
            return getProperty(ExtraAccess.class);
        }

        default void setExtra(T extra) {
            setProperty(ExtraAccess.class, extra);
        }
    }

    interface AccessAdapter<T extends DomainObject, E> extends ContextAccess,
            ConditionsAccess, RepositoryAccess<T>, ExtraAccess<E> {

        @Override
        default DomainContext getContext() {
            if (getCollection() instanceof ContextAccess) {
                return ((ContextAccess) getCollection()).getContext();
            }
            throw new UnsupportedOperationException("Can not access DomainContext");
        }

        @Override
        default Conditions getConditions() {
            if (getCollection() instanceof ConditionsAccess) {
                return ((ConditionsAccess) getCollection()).getConditions();
            }
            throw new UnsupportedOperationException("Can not access Conditions");
        }

        @SuppressWarnings("unchecked")
        @Override
        default DomainRepository<T, ?> getRepository() {
            if (getCollection() instanceof RepositoryAccess) {
                return ((RepositoryAccess<T>) getCollection()).getRepository();
            }
            throw new UnsupportedOperationException("Can not access DomainRepository");
        }

        @SuppressWarnings("unchecked")
        @Override
        default E getExtra() {
            if (getCollection() instanceof ExtraAccess) {
                return ((ExtraAccess<E>) getCollection()).getExtra();
            }
            throw new UnsupportedOperationException("Can not access Extra");
        }

        @SuppressWarnings("unchecked")
        @Override
        default void setExtra(E extra) {
            if (getCollection() instanceof ExtraAccess) {
                ((ExtraAccess<E>) getCollection()).setExtra(extra);
                return;
            }
            throw new UnsupportedOperationException("Can not access Extra");
        }

        DomainCollection<T> getCollection();
    }
}
