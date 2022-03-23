package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.convert.MapToObjectConvertor;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.match.ContentMatcher;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

@NoArgsConstructor
@AllArgsConstructor
public abstract class ContentExtractor<T> extends TypeMetadataPluginExtractor<T> {

    private Charset charset;

    public ContentExtractor(String charset) {
        this.charset = Charset.forName(charset);
    }

    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> target = metadata.getTargetClass();
        if (metadata.isArray() && target == byte.class) {
            return new ContentMatcher(byte[].class, charset, annotations, new MapToObjectConvertor());
        }
        if (target == String.class || InputStream.class.isAssignableFrom(target)) {
            PluginConvertor convertor = getConvertorAdapter().adapt(metadata);
            return new ContentMatcher(target, charset, annotations, convertor);
        }
        return null;
    }
}
