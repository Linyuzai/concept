package com.github.linyuzai.download.core.source.reflection;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@Getter
public class ReflectionSource implements Source {

    private final ReflectionCache cache;

    private final Object target;

    private final Source source;

    private final Map<Class<? extends Annotation>, Object> valueMap = new HashMap<>();

    public ReflectionSource(ReflectionCache cache, Object target, Source source) {
        this.cache = cache;
        this.target = target;
        this.source = source;
    }

    protected Object reflect(Class<? extends Annotation> clazz) {
        try {
            if (valueMap.containsKey(clazz)) {
                return valueMap.get(clazz);
            }
            Object o = cache.reflect(clazz, target);
            valueMap.put(clazz, o);
            return o;
        } catch (ReflectiveOperationException e) {
            throw new DownloadException(e);
        }
    }

    @Override
    public String getName() {
        Object name = reflect(SourceName.class);
        if (name == null) {
            return source.getName();
        } else {
            return String.valueOf(name);
        }
    }

    @Override
    public Charset getCharset() {
        Object charset = reflect(SourceCharset.class);
        if (charset == null) {
            return source.getCharset();
        } else {
            if (charset instanceof Charset) {
                return (Charset) charset;
            } else {
                return Charset.forName(String.valueOf(charset));
            }
        }
    }

    @Override
    public long getLength() {
        Object length = reflect(SourceLength.class);
        if (length == null) {
            return source.getLength();
        } else {
            return Long.parseLong(String.valueOf(length));
        }
    }

    @Override
    public boolean isAsyncLoad() {
        Object asyncLoad = reflect(SourceAsyncLoad.class);
        if (asyncLoad == null) {
            return source.isAsyncLoad();
        } else {
            return Boolean.parseBoolean(String.valueOf(asyncLoad));
        }
    }

    @Override
    public boolean isCacheEnabled() {
        Object cacheEnabled = reflect(SourceCacheEnabled.class);
        if (cacheEnabled == null) {
            return source.isCacheEnabled();
        } else {
            return Boolean.parseBoolean(String.valueOf(cacheEnabled));
        }
    }

    @Override
    public boolean isCacheExisted() {
        Object cacheExisted = reflect(SourceCacheExisted.class);
        if (cacheExisted == null) {
            return source.isCacheExisted();
        } else {
            return Boolean.parseBoolean(String.valueOf(cacheExisted));
        }
    }

    @Override
    public String getCachePath() {
        Object cachePath = reflect(SourceCachePath.class);
        if (cachePath == null) {
            return source.getCachePath();
        } else {
            return String.valueOf(cachePath);
        }
    }

    @Override
    public void deleteCache() {
        source.deleteCache();
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer) throws IOException {
        source.write(os, range, writer);
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        source.write(os, range, writer);
    }

    @Override
    public void load(DownloadContext context) throws IOException {
        source.load(context);
    }

    @Override
    public boolean isSingle() {
        return source.isSingle();
    }

    @Override
    public Collection<Source> list() {
        return source.list();
    }

    @Override
    public Collection<Source> list(Predicate<Source> predicate) {
        return source.list(predicate);
    }
}
