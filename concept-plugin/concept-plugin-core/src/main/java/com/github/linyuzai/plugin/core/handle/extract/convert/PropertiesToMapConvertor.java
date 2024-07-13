package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * {@link Properties} 转 {@link Map}
 */
@Deprecated
@Getter
@RequiredArgsConstructor
public class PropertiesToMapConvertor extends AbstractPluginConvertor<Supplier<Properties>, Map<String, String>> {

    /**
     * {@link Map} 类型
     */
    private final Class<?> mapClass;

    @Override
    public Map<String, String> doConvert(Supplier<Properties> supplier) {
        Properties properties = supplier.get();
        Map<String, String> propertiesToMap = ReflectionUtils.newMap(mapClass);
        for (String propertyName : properties.stringPropertyNames()) {
            propertiesToMap.put(propertyName, properties.getProperty(propertyName));
        }
        return propertiesToMap;
    }
}
