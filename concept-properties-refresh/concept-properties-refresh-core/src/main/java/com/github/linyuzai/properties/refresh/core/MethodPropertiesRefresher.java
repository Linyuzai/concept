package com.github.linyuzai.properties.refresh.core;

import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import com.github.linyuzai.properties.refresh.core.exception.PropertiesRefreshException;
import lombok.Getter;
import lombok.SneakyThrows;

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
    private final Map<Integer, KeyTypePair> keyTypePairMap;

    /**
     * 方法参数个数
     */
    private final int parameterCount;

    public MethodPropertiesRefresher(
            Object target,
            Method method) {
        super(target);
        this.method = method;
        this.parameterCount = method.getParameterCount();
        //必须有方法参数
        if (parameterCount == 0) {
            throw new PropertiesRefreshException("No refreshable properties found");
        }
        Parameter[] parameters = this.method.getParameters();
        Type[] parameterTypes = this.method.getGenericParameterTypes();
        this.keyTypePairMap = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            RefreshableProperties annotation = parameters[i].getAnnotation(RefreshableProperties.class);
            if (annotation == null) {
                //如果方法参数上没有注解，则key按照空字符串匹配
                keyTypePairMap.put(i, new KeyTypePair("", parameterTypes[i]));
            } else {
                keyTypePairMap.put(i, new KeyTypePair(annotation.value(), parameterTypes[i]));
            }
        }

    }

    /**
     * 判断是否需要刷新回调
     * 只要有一个参数需要刷新就需要回调
     *
     * @param condition 刷新条件
     * @return 是否需要刷新回调
     */
    //@Override
    public boolean needRefresh(RefreshPropertiesCondition condition) {
        Collection<KeyTypePair> values = keyTypePairMap.values();
        for (KeyTypePair pair : values) {
            if (condition.match(pair)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doRefresh(RefreshPropertiesCondition condition) {

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
    public void refresh(PlatformProperties properties) {
        Object[] values = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            KeyTypePair pair = keyTypePairMap.get(i);
            if (pair != null) {
                if (pair.getType() instanceof Class && ((Class<?>) pair.getType()).isInstance(target)) {
                    //如果方法参数的类型是target，则直接赋值target
                    values[i] = target;
                } else {
                    values[i] = getValue(pair, properties);
                }
            }
        }
        invokeMethod(method, values);
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
        method.invoke(target, values);
    }
}
