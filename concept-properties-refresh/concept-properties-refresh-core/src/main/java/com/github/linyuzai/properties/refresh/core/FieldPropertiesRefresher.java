package com.github.linyuzai.properties.refresh.core;

import com.github.linyuzai.properties.refresh.core.condition.RefreshPropertiesCondition;
import com.github.linyuzai.properties.refresh.core.resolver.PropertiesResolver;
import com.github.linyuzai.properties.refresh.core.resolver.PropertiesResolverAdapter;
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
    private final String key;

    private final Type type;

    private final PropertiesResolver resolver;

    public FieldPropertiesRefresher(
            Field field,
            Object target,
            PropertiesResolverAdapter adapter) {
        super(target);
        this.field = field;
        RefreshableProperties annotation = this.field.getAnnotation(RefreshableProperties.class);
        this.key = annotation.value();
        this.type = field.getGenericType();
        this.resolver = adapter.getResolver(key, type);
        this.field.setAccessible(true);
    }

    @SneakyThrows
    @Override
    public void doRefresh(RefreshPropertiesCondition condition, Object target) {
        boolean needOldValue = condition.needOldValue();
        boolean needNewValue = condition.needNewValue();
        Object oldValue;
        if (needOldValue) {
            oldValue = field.get(target);
        } else {
            oldValue = null;
        }
        Object newValue;
        if (needNewValue) {
            newValue = resolver.resolve(key, type);
        } else {
            newValue = null;
        }
        if (condition.match(key, type, oldValue, newValue)) {
            Object value;
            if (needNewValue) {
                value = newValue;
            } else {
                value = resolver.resolve(key, type);
            }
            if (value != null || condition.refreshOnNull()) {
                field.set(target, value);
            }
        }
    }
}
