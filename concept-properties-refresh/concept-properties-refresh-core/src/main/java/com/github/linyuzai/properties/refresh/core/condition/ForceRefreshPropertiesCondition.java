package com.github.linyuzai.properties.refresh.core.condition;

import com.github.linyuzai.properties.refresh.core.KeyTypePair;

import java.lang.reflect.Type;

/**
 * 强制刷新/全量刷新
 */
public class ForceRefreshPropertiesCondition implements RefreshPropertiesCondition {

    @Override
    public boolean match(String key, Type type, Object oldValue, Object newValue) {
        return true;
    }
}
