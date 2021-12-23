package com.github.linyuzai.download.core.source.proxy;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.load.Loadable;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ProxySourceMethodInterceptor implements MethodInterceptor {

    private final Object model;

    private final ReflectionTemplate template;

    private final Map<Class<? extends Annotation>, Object> valueMap = new HashMap<>();

    public ProxySourceMethodInterceptor(Object model, ReflectionTemplate template) {
        this.model = model;
        this.template = template;
    }

    public Object reflect(Class<? extends Annotation> clazz) {
        return valueMap.computeIfAbsent(clazz, this::reflect0);
    }

    private Object reflect0(Class<? extends Annotation> clazz) {
        return template.reflect(clazz, model);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (isSourceNameMethod(method)) {
            Object name = reflect(SourceName.class);
            if (name == null) {
                return proxy.invokeSuper(obj, args);
            } else {
                return String.valueOf(name);
            }
        }

        if (isSourceCharsetMethod(method)) {
            Object charset = reflect(SourceCharset.class);
            if (charset == null) {
                return proxy.invokeSuper(obj, args);
            } else {
                if (charset instanceof Charset) {
                    return charset;
                } else {
                    return Charset.forName(String.valueOf(charset));
                }
            }
        }

        if (isSourceLengthMethod(method)) {
            Object length = reflect(SourceLength.class);
            if (length == null) {
                return proxy.invokeSuper(obj, args);
            } else {
                return Long.parseLong(String.valueOf(length));
            }
        }

        if (isSourceAsyncLoadMethod(method)) {
            Object asyncLoad = reflect(SourceAsyncLoad.class);
            if (asyncLoad == null) {
                return proxy.invokeSuper(obj, args);
            } else {
                return Boolean.parseBoolean(String.valueOf(asyncLoad));
            }
        }

        if (isSourceCacheEnabledMethod(method)) {
            Object cacheEnabled = reflect(SourceCacheEnabled.class);
            if (cacheEnabled == null) {
                return proxy.invokeSuper(obj, args);
            } else {
                return Boolean.parseBoolean(String.valueOf(cacheEnabled));
            }
        }

        if (isSourceCacheExistedMethod(method)) {
            Object cacheExisted = reflect(SourceCacheExisted.class);
            if (cacheExisted == null) {
                return proxy.invokeSuper(obj, args);
            } else {
                return Boolean.parseBoolean(String.valueOf(cacheExisted));
            }
        }

        if (isSourceCachePathMethod(method)) {
            Object cachePath = reflect(SourceCachePath.class);
            if (cachePath == null) {
                return proxy.invokeSuper(obj, args);
            } else {
                return String.valueOf(cachePath);
            }
        }

        return proxy.invokeSuper(obj, args);
    }

    public static boolean isSourceNameMethod(Method method) throws NoSuchMethodException {
        return isMethodEquals(Downloadable.class, "getName", method);
    }

    public static boolean isSourceCharsetMethod(Method method) throws NoSuchMethodException {
        return isMethodEquals(Downloadable.class, "getCharset", method);
    }

    public static boolean isSourceLengthMethod(Method method) throws NoSuchMethodException {
        return isMethodEquals(Downloadable.class, "getLength", method);
    }

    public static boolean isSourceAsyncLoadMethod(Method method) throws NoSuchMethodException {
        return isMethodEquals(Loadable.class, "isAsyncLoad", method);
    }

    public static boolean isSourceCacheEnabledMethod(Method method) throws NoSuchMethodException {
        return isMethodEquals(Cacheable.class, "isCacheEnabled", method);
    }

    public static boolean isSourceCacheExistedMethod(Method method) throws NoSuchMethodException {
        return isMethodEquals(Cacheable.class, "isCacheExisted", method);
    }

    public static boolean isSourceCachePathMethod(Method method) throws NoSuchMethodException {
        return isMethodEquals(Cacheable.class, "getCachePath", method);
    }

    public static boolean isMethodEquals(Class<?> clazz, String name, Method method) throws NoSuchMethodException {
        Method other = clazz.getMethod(name);
        return method.getName().equals(name) &&
                method.getReturnType().equals(other.getReturnType()) &&
                equalParamTypes(method.getParameterTypes(), other.getParameterTypes());
    }

    private static boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; i++) {
                if (params1[i] != params2[i])
                    return false;
            }
            return true;
        }
        return false;
    }
}
