package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * {@link Properties} 转 {@link Map} 的转换器
 */
@Setter
@Getter
@AllArgsConstructor
public class PropertiesToMapConvertor extends AbstractPluginConvertor<Supplier<Properties>, Map<String, String>> {

    /**
     * {@link Map} 类型
     */
    private Class<?> mapClass;

    /**
     * 将所有的 {@link Properties} 转为 {@link Map}
     *
     * @param supplier value 类型为 {@link Properties} 的 {@link Map}
     * @return value 类型为 {@link Map} 的 {@link Map}
     */
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
