package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Setter
@Getter
@AllArgsConstructor
public class PropertiesToMapMapConvertor extends AbstractPluginConvertor<Map<?, Properties>, Map<Object, Map<String, String>>> {

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
