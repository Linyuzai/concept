package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.resolve.ByteArrayPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@DependOnResolvers(ByteArrayPluginResolver.class)
public class ContentMatcher extends AbstractPluginMatcher<Map<String, byte[]>> {

    public ContentMatcher(Annotation[] annotations) {
        super(annotations);
    }

    @Override
    public Object getKey() {
        return Plugin.BYTE_ARRAY;
    }

    @Override
    public Map<String, byte[]> filter(Map<String, byte[]> bytesMap) {
        Map<String, byte[]> map = new LinkedHashMap<>();
        for (Map.Entry<String, byte[]> entry : bytesMap.entrySet()) {
            if (filterWithAnnotation(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    @Override
    public boolean isEmpty(Map<String, byte[]> filter) {
        return filter.isEmpty();
    }
}
