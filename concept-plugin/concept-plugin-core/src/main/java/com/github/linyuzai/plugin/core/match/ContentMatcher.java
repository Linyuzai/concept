package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.resolve.ContentResolver;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 内容匹配器
 */
@HandlerDependency(ContentResolver.class)
public class ContentMatcher extends AbstractPluginMatcher<Map<Object, byte[]>> {

    public ContentMatcher(Annotation[] annotations) {
        super(annotations);
    }

    @Override
    public Object getKey() {
        return Plugin.BYTE_ARRAY;
    }

    @Override
    public Map<Object, byte[]> filter(Map<Object, byte[]> bytesMap) {
        Map<Object, byte[]> map = new LinkedHashMap<>();
        for (Map.Entry<Object, byte[]> entry : bytesMap.entrySet()) {
            Object key = entry.getKey();
            if (key instanceof String) {
                if (filterWithAnnotation((String) key)) {
                    map.put(entry.getKey(), entry.getValue());
                }
            } else {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    @Override
    public boolean isEmpty(Map<Object, byte[]> filtered) {
        return filtered.isEmpty();
    }
}
