package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.resolve.ByteArrayPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@DependOnResolvers(ByteArrayPluginResolver.class)
public class ContentMatcher extends AbstractPluginMatcher<Map<String, byte[]>, Map<String, Object>> {

    protected final Class<?> target;

    protected final Charset charset;

    protected Function<byte[], Object> function;

    public ContentMatcher(Class<?> target, Charset charset, Annotation[] annotations) {
        super(annotations);
        this.target = target;
        this.charset = charset;
        if (byte[].class != target) {
            if (InputStream.class == target) {
                function = ByteArrayInputStream::new;
            } else if (String.class == target) {
                function = bytes -> charset == null ? new String(bytes) : new String(bytes, charset);
            }
        }
    }

    @Override
    public Object getKey() {
        return Plugin.BYTE_ARRAY;
    }

    @Override
    public Map<String, Object> filter(Map<String, byte[]> bytesMap) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, byte[]> entry : bytesMap.entrySet()) {
            if (function == null) {
                map.put(entry.getKey(), entry.getValue());
            } else {
                map.put(entry.getKey(), function.apply(entry.getValue()));
            }
        }
        return map;
    }

    @Override
    public boolean isEmpty(Map<String, Object> filter) {
        return filter.isEmpty();
    }
}
