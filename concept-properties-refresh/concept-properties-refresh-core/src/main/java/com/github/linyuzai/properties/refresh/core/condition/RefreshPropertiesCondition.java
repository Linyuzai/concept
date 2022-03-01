package com.github.linyuzai.properties.refresh.core.condition;

import com.github.linyuzai.properties.refresh.core.KeyTypePair;

import java.lang.reflect.Type;

/**
 * 配置属性刷新条件
 */
public interface RefreshPropertiesCondition {

    /**
     * 是否匹配刷新条件
     *
     * @return 是否匹配刷新条件
     */
    boolean match(KeyTypePair pair);
}
