package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.resolve.ByteArrayPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@DependOnResolvers(ByteArrayPluginResolver.class)
public abstract class ContentMatcher extends AbstractPluginMatcher<byte[]> {

    protected final Class<?> target;

    protected final Charset charset;

    protected Function<byte[], Object> function;

    public ContentMatcher(Class<?> target, Charset charset, Annotation[] annotations) {
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

    @Getter
    public static class MapMatcher extends ContentMatcher implements MapConvertor {

        private final Class<?> mapClass;

        public MapMatcher(Class<?> mapClass, Class<?> target, Charset charset, Annotation[] annotations) {
            super(target, charset, annotations);
            this.mapClass = mapClass;
        }
    }

    @Getter
    public static class ListMatcher extends ContentMatcher implements ListConvertor {

        private final Class<?> listClass;

        public ListMatcher(Class<?> listClass, Class<?> target, Charset charset, Annotation[] annotations) {
            super(target, charset, annotations);
            this.listClass = listClass;
        }
    }

    @Getter
    public static class SetMatcher extends ContentMatcher implements SetConvertor {

        private final Class<?> setClass;

        public SetMatcher(Class<?> setClass, Class<?> target, Charset charset, Annotation[] annotations) {
            super(target, charset, annotations);
            this.setClass = setClass;
        }
    }

    public static class ArrayMatcher extends ContentMatcher implements ArrayConvertor {

        public ArrayMatcher(Class<?> target, Charset charset, Annotation[] annotations) {
            super(target, charset, annotations);
        }

        @Override
        public Class<?> getArrayClass() {
            return target;
        }
    }

    public static class ObjectMatcher extends ContentMatcher implements ObjectConvertor {

        public ObjectMatcher(Class<?> target, Charset charset, Annotation[] annotations) {
            super(target, charset, annotations);
        }

        @Override
        public String getType() {
            return "content";
        }
    }
}
