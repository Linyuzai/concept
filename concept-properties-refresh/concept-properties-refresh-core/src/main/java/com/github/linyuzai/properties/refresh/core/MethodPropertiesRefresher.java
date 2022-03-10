package com.github.linyuzai.properties.refresh.core;

import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import com.github.linyuzai.properties.refresh.core.exception.PropertiesRefreshException;
import com.github.linyuzai.properties.refresh.core.resolver.PropertiesResolver;
import com.github.linyuzai.properties.refresh.core.resolver.PropertiesResolverAdapter;
import lombok.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 触发方法回调的刷新器
 *
 * @see PropertiesRefresher
 * @see OnPropertiesRefresh
 */
@Getter
public class MethodPropertiesRefresher extends AbstractPropertiesRefresher {

    /**
     * 需要回调的方法
     */
    private final Method method;

    /**
     * 方法参数对应的匹配要求
     */
    private final Map<Integer, Key_Type_Resolver> map;

    /**
     * 方法参数个数
     */
    private final int parameterCount;

    public MethodPropertiesRefresher(
            Method method,
            Object target,
            PropertiesResolverAdapter adapter) {
        super(target);
        this.method = method;
        this.parameterCount = method.getParameterCount();
        //必须有方法参数
        if (parameterCount == 0) {
            throw new PropertiesRefreshException("No refreshable properties found");
        }
        Parameter[] parameters = this.method.getParameters();
        Type[] parameterTypes = this.method.getGenericParameterTypes();
        this.map = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            RefreshableProperties annotation = parameters[i].getAnnotation(RefreshableProperties.class);
            if (annotation == null) {
                continue;
            }
            String key = annotation.value();
            Type type = parameterTypes[i];
            PropertiesResolver resolver = adapter.getResolver(key, type);
            map.put(i, new Key_Type_Resolver(key, type, resolver));
        }
        this.method.setAccessible(true);
    }

    /**
     * 刷新，将方法参数按照需要的要求进行值绑定
     * 然后调用方法
     *
     * @param properties 配置属性源
     * @throws Throwable 异常
     */
    @SneakyThrows
    @Override
    public void doRefresh(RefreshPropertiesCondition condition, Object target) {
        //boolean needOldValue = condition.needOldValue();
        boolean needNewValue = condition.needNewValue();

        boolean needRefresh = false;

        Object[] values = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            Key_Type_Resolver ktr = map.get(i);
            if (ktr == null) {
                continue;
            }
            String key = ktr.key;
            Type type = ktr.type;

            Object newValue;
            if (needNewValue) {
                if (ktr.getType() instanceof Class && ((Class<?>) ktr.getType()).isInstance(target)) {
                    //如果方法参数的类型是target，则直接赋值target
                    newValue = target;
                } else {
                    newValue = ktr.resolver.resolve(key, type);
                }
            } else {
                newValue = null;
            }
            values[i] = newValue;
            if (condition.match(key, type, null, newValue)) {
                needRefresh = true;
            }
        }
        if (needRefresh) {
            if (!needNewValue) {
                for (int i = 0; i < parameterCount; i++) {
                    Key_Type_Resolver ktr = map.get(i);
                    if (ktr == null) {
                        continue;
                    }
                    String key = ktr.key;
                    Type type = ktr.type;
                    if (ktr.getType() instanceof Class && ((Class<?>) ktr.getType()).isInstance(target)) {
                        //如果方法参数的类型是target，则直接赋值target
                        values[i] = target;
                    } else {
                        values[i] = ktr.resolver.resolve(key, type);
                    }
                }
            }
            method.invoke(target, values);
        }
    }

    /**
     * 调用方法
     *
     * @param method 被调用的方法
     * @param values 方法参数值
     * @throws Throwable 异常
     */
    public void invokeMethod(Method method, Object[] values) throws Throwable {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key_Type_Resolver {

        /**
         * 匹配的key
         */
        private String key;

        /**
         * 属性的类型
         */
        private Type type;

        private PropertiesResolver resolver;
    }
}
