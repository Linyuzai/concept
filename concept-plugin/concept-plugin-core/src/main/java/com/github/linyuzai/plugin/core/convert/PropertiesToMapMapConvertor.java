package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Properties} 转 {@link Map} 的转换器
 */
@Setter
@Getter
@AllArgsConstructor
public class PropertiesToMapMapConvertor extends AbstractPluginConvertor<Map<?, Properties>, Map<Object, Map<String, String>>> {

    /**
     * {@link Map} 类型
     */
    private Class<?> mapClass;

    @Override
    public Map<Object, Map<String, String>> doConvert(Map<?, Properties> source) {
        Map<Object, Map<String, String>> map = new LinkedHashMap<>();
        for (Map.Entry<?, Properties> entry : source.entrySet()) {
            Map<String, String> propertiesToMap = ReflectionUtils.newMap(mapClass);
            Properties properties = entry.getValue();
            for (String propertyName : properties.stringPropertyNames()) {
                propertiesToMap.put(propertyName, properties.getProperty(propertyName));
            }
            map.put(entry.getKey(), propertiesToMap);
        }
        return map;
    }
}
