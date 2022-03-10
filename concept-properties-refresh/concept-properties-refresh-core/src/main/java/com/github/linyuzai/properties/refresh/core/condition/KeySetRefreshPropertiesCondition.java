package com.github.linyuzai.properties.refresh.core.condition;

import com.github.linyuzai.properties.refresh.core.KeyTypePair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * 通过变更的属性的key来判断是否需要刷新
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeySetRefreshPropertiesCondition implements RefreshPropertiesCondition {

    /**
     * 配置属性变更的key
     */
    private Set<String> keys;

    //@Override
    public boolean match(KeyTypePair pair) {
        for (String key : keys) {
            //相等或Map情况下前缀匹配
            if (key.startsWith(pair.getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean match(String key, Type type, Object oldValue, Object newValue) {
        return false;
    }
}
