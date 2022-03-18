package com.github.linyuzai.plugin.core.extract;

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
    public PluginMatcher getMatcher(TypeMetadata metadata, Class<?> target, Annotation[] annotations) {
        if (metadata.isArray() && target == byte.class) {
            return new ContentMatcher.ObjectMatcher(byte[].class, charset, annotations);
        }
        if (target == String.class || InputStream.class.isAssignableFrom(target)) {
            if (metadata.isMap()) {
                return new ContentMatcher.MapMatcher(metadata.getMapClass(), target, charset, annotations);
            } else if (metadata.isList()) {
                return new ContentMatcher.ListMatcher(metadata.getListClass(), target, charset, annotations);
            } else if (metadata.isSet()) {
                return new ContentMatcher.SetMatcher(metadata.getSetClass(), target, charset, annotations);
            } else if (metadata.isCollection()) {
                return new ContentMatcher.ListMatcher(metadata.getCollectionClass(), target, charset, annotations);
            } else if (metadata.isArray()) {
                return new ContentMatcher.ArrayMatcher(target, charset, annotations);
            } else {
                return new ContentMatcher.ObjectMatcher(target, charset, annotations);
            }
        }
        return null;
    }
}
