package com.github.linyuzai.properties.refresh.core.condition;

import com.github.linyuzai.properties.refresh.core.KeyTypePair;

/**
 * 强制刷新/全量刷新
 */
public class ForceRefreshPropertiesCondition implements RefreshPropertiesCondition {

    @Override
    public boolean match(KeyTypePair pair) {
        return true;
    }
}
