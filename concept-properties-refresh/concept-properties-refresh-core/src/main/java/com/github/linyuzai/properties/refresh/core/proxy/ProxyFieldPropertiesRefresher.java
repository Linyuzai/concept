package com.github.linyuzai.properties.refresh.core.proxy;

import com.github.linyuzai.properties.refresh.core.AbstractPropertiesRefresher;
import com.github.linyuzai.properties.refresh.core.KeyTypePair;
import com.github.linyuzai.properties.refresh.core.PlatformProperties;
import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 获得属性的方法当作字段
 */
@Getter
public class ProxyFieldPropertiesRefresher extends AbstractPropertiesRefresher {

    /**
     * 对应的方法
     */
    private final Method method;

    /**
     * 值缓存
     */
    private final Map<Method, Object> valueMap;

    /**
     * 返回值类型和匹配的key
     */
    private final KeyTypePair keyTypePair;

    public ProxyFieldPropertiesRefresher(
            String key,
            Type type,
            Method method,
            Map<Method, Object> valueMap) {
        super(null);
        this.method = method;
        this.valueMap = valueMap;
        this.keyTypePair = new KeyTypePair(key, type);
    }

    /**
     * 是否需要刷新
     *
     * @param condition 刷新条件
     * @return 是否需要刷新
     */
    //@Override
    public boolean needRefresh(RefreshPropertiesCondition condition) {
        //return condition.match(keyTypePair);
        return false;
    }

    //@Override
    public void doRefresh(RefreshPropertiesCondition condition) {

    }

    /**
     * 更新缓存中的值
     *
     * @param properties 配置属性源
     * @throws Throwable 异常
     */
    //@Override
    public void refresh(PlatformProperties properties) {
        Object value = getValue(keyTypePair, properties);
        if (value != null) {
            valueMap.put(method, value);
        }
    }

    @Override
    public void doRefresh(RefreshPropertiesCondition condition, Object target) {

    }
}
