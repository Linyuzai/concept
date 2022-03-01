package com.github.linyuzai.properties.refresh.core.proxy;

import com.github.linyuzai.properties.refresh.core.MethodPropertiesRefresher;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

/**
 * 代理配置属性接口的方法回调
 *
 * @see MethodPropertiesRefresher
 */
public class ProxyMethodPropertiesRefresher extends MethodPropertiesRefresher {

    /**
     * 默认方法句柄
     */
    private final MethodHandle methodHandle;

    public ProxyMethodPropertiesRefresher(Object target, Method method, MethodHandle methodHandle) {
        super(target, method);
        this.methodHandle = methodHandle;
    }

    /**
     * 调用默认方法
     *
     * @param method 被调用的方法
     * @param values 方法参数值
     * @throws Throwable 异常
     */
    @Override
    public void invokeMethod(Method method, Object[] values) throws Throwable {
        methodHandle.invokeWithArguments(values);
    }
}
