/*
package com.github.linyuzai.properties.refresh.core.proxy;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

*/
/**
 * 可刷新的代理配置属性
 * 提供和 {@link RefreshablePlatformProperties} 一样的功能
 *//*

public abstract class RefreshableProxyPlatformProperties extends ProxyPlatformProperties {

    */
/**
     * 刷新器管理器
     *//*

    private final PropertiesRefresherManager refresherManager = new PropertiesRefresherManager();

    */
/**
     * 属性缓存
     *//*

    private final Map<Method, Object> refreshableValueMap = new ConcurrentHashMap<>();

    */
/**
     * 实例对象，默认为null
     *//*

    @Getter
    @Setter
    private Object target;

    */
/**
     * 是否刷新字段
     *//*

    @Getter
    @Setter
    private boolean refreshFields = true;

    */
/**
     * 是否回调方法
     *//*

    @Getter
    @Setter
    private boolean refreshMethods = true;

    */
/**
     * 重写方法处理，使用刷新器 {@link PropertiesRefresher} 来处理
     *
     * @param proxy   代理对象
     * @param methods 方法列表
     * @throws Throwable 异常
     *//*

    @Override
    public void registerMethods(Object proxy, List<Method> methods) throws Throwable {
        for (Method method : methods) {
            handleMethodIfDefault(method, proxy);
            if (refreshFields) {
                RefreshableProperties fieldAnnotation =
                        method.getAnnotation(RefreshableProperties.class);
                if (fieldAnnotation != null) {
                    ProxyFieldPropertiesRefresher refresher = new ProxyFieldPropertiesRefresher(
                            fieldAnnotation.value(), method.getGenericReturnType(),
                            method, refreshableValueMap);
                    refresherManager.addRefresher(refresher);
                    addProxyMetadata(method, refresher.getMetadata());
                }
            }
            if (refreshMethods) {
                OnPropertiesRefresh methodAnnotation =
                        method.getAnnotation(OnPropertiesRefresh.class);
                if (methodAnnotation != null) {
                    if (method.isDefault()) {
                        PropertiesRefresher refresher = new ProxyMethodPropertiesRefresher(
                                target, method, getMethodHandle(method, proxy));
                        refresherManager.addRefresher(refresher);
                    } else {
                        throw new PlatformPropertiesException("Proxy method must have default impl: " + method);
                    }
                }
            }
        }
    }

    */
/**
     * 从缓存获得当前的值
     *
     * @param metadata 类型及匹配的key
     * @param method   方法
     * @return 缓存的值
     *//*

    @Override
    public Object getProxyValue(Metadata metadata, Method method) {
        return refreshableValueMap.get(method);
    }

    */
/**
     * 使用当前配置属性进行刷新
     *//*

    @Override
    public void refresh() {
        refresherManager.refresh(this);
    }

    */
/**
     * 使用当前配置属性进行条件刷新
     *
     * @param condition 刷新条件
     *//*

    @Override
    public void refresh(RefreshPropertiesCondition condition) {
        refresherManager.refresh(this, condition);
    }

    @Override
    public void destroy() {
        super.destroy();
        //清空缓存
        refresherManager.destroy();
        refreshableValueMap.clear();
    }
}
*/
