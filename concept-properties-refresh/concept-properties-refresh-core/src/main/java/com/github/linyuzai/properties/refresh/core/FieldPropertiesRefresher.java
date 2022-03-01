package com.github.linyuzai.properties.refresh.core;

import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 自动刷新字段的刷新器
 *
 * @see PropertiesRefresher
 * @see RefreshableProperties
 */
@Getter
public class FieldPropertiesRefresher extends AbstractPropertiesRefresher {

    /**
     * 需要自动刷新的字段
     */
    private final Field field;

    /**
     * 值的类型和匹配key
     */
    private final KeyTypePair keyTypePair;

    public FieldPropertiesRefresher(
            String key,
            Object target,
            Field field) {
        this(key, field.getGenericType(), target, field);
    }

    public FieldPropertiesRefresher(
            String key,
            Type type,
            Object target,
            Field field) {
        super(target);
        this.field = field;
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
        return condition.match(keyTypePair);
    }

    @Override
    public void doRefresh(RefreshPropertiesCondition condition) {

    }

    /**
     * 刷新，如果值为null则不覆盖，可用于支持默认值
     *
     * @param properties 配置属性源
     * @throws Throwable 异常
     */
    @SneakyThrows
    @Override
    public void refresh(PlatformProperties properties) {
        Object value = getValue(keyTypePair, properties);
        if (value != null) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(target, value);
        }
    }
}
