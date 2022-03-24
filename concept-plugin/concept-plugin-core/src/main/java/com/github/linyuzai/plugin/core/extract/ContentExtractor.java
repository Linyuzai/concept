package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.format.MapToObjectFormatter;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
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
            return new ContentMatcher(byte[].class, charset, annotations);
        }
        if (target == String.class || InputStream.class.isAssignableFrom(target)) {
            return new ContentMatcher(target, charset, annotations);
        }
        return null;
    }

    @Override
    public PluginFormatter getFormatter(TypeMetadata metadata, Annotation[] annotations) {
        if (metadata.isArray() && metadata.getTargetClass() == byte.class) {
            return new MapToObjectFormatter();
        }
        return super.getFormatter(metadata, annotations);
    }
}
