package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.io.*;

/**
 * 需要预加载资源的抽象类 / Abstract classes that require preloaded resources
 * 需要注意数据流只能使用一次 / Note that stream can only be used once
 */
@Getter
public abstract class AbstractLoadableSource extends AbstractSource {

    protected Mono<InputStream> inputStream;

    /**
     * 如果需要异步加载 / If asynchronous loading is required
     * 在启用缓存并存在缓存的情况下 / With cache enabled and cache present
     * 直接同步加载本地缓存 / Loading local cache synchronous
     *
     * @return 是否异步加载 / If async load
     */
    public boolean isAsyncLoad() {
        boolean asyncLoad = super.isAsyncLoad();
        if (asyncLoad && isCacheEnabled() && isCacheExisted()) {
            return false;
        }
        return asyncLoad;
    }

    /**
     * @return 本地文件是否存在
     */
    @Override
    public boolean isCacheExisted() {
        return new File(getCachePath(), getName()).exists();
    }

    /**
     * 不启用缓存，则直接加载 / If caching is not enabled, load directly
     * 启用缓存并缓存存在，则使用缓存 / Cache is used if cache is enabled and exists
     * 启用缓存并缓存不存在，则加载到缓存并使用缓存 / Load into cache and use cache if cache is enabled and cache does not exist,
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public void load(DownloadContext context) {
        if (isCacheEnabled()) {
            String cachePath = getCachePath();
            if (cachePath == null) {
                throw new DownloadException("Cache path is null");
            }
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            String name = getName();
            if (name == null || name.isEmpty()) {
                CacheNameGenerator generator = context.get(CacheNameGenerator.class);
                name = generator.generate(this, context);
                if (name == null || name.isEmpty()) {
                    throw new DownloadException("Cache name is null or empty");
                }
            }
            File cache = new File(dir, name);
            inputStream = (cache.exists() ? Mono.just(cache) : doLoad(context).map(it -> {
                DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
                DownloadWriter writer = writerAdapter.getWriter(this, null, context);
                try (InputStream is = it;
                     FileOutputStream fos = new FileOutputStream(cache)) {
                    writer.write(is, fos, null, getCharset(), getLength());
                    return cache;
                } catch (Throwable e) {
                    return Mono.error(e);
                }
            })).map(it -> {
                String contentType = getContentType();
                if (contentType == null || contentType.isEmpty()) {
                    setContentType(ContentType.file(cache));
                }
                try {
                    return new FileInputStream(cache);
                } catch (FileNotFoundException e) {
                    throw new DownloadException(e);
                }
            });
        } else {
            inputStream = doLoad(context).map(is -> {
                DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
                DownloadWriter writer = writerAdapter.getWriter(this, null, context);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                writer.write(is, os, null, getCharset(), getLength());
                return new ByteArrayInputStream(os.toByteArray());
            });
        }
    }

    /**
     * 删除缓存
     */
    @Override
    public void deleteCache() {
        if (isCacheEnabled()) {
            File file = new File(getCachePath(), getName());
            if (file.exists()) {
                boolean delete = file.delete();
            }
        }
    }

    /**
     * 加载输入流 / Load input stream
     *
     * @param context 下载上下文 / Context of download
     * @return 输入流 / Input stream
     */
    public abstract Mono<InputStream> doLoad(DownloadContext context);

    public static abstract class Builder<T extends AbstractLoadableSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        @Override
        protected T build(T target) {
            return super.build(target);
        }
    }
}
