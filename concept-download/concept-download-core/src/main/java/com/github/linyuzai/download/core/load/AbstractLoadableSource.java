package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.ContentType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.io.*;

/**
 * 需要预加载资源的抽象类 / Abstract classes that require preloaded resources
 * 需要注意数据流只能使用一次 / Note that stream can only be used once
 */
@Getter
public abstract class AbstractLoadableSource extends AbstractSource {

    protected Long length;

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
    @SneakyThrows
    @Override
    public Mono<Source> load(DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        if (inputStream != null) {
            publisher.publish(new SourceAlreadyLoadedEvent(context, this));
            return Mono.just(this);
        }
        if (isCacheEnabled()) {
            String cachePath = getCachePath();
            if (cachePath == null) {
                throw new DownloadException("Cache path is null");
            }
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            CacheNameGenerator generator = context.get(CacheNameGenerator.class);
            String nameToUse = generator.generate(this, context);
            if (!StringUtils.hasText(nameToUse)) {
                throw new DownloadException("Cache name is null or empty");
            }

            File cache = new File(dir, nameToUse);
            Mono<Source> mono;
            if (cache.exists()) {
                publisher.publish(new SourceLoadedCacheUsedEvent(context, this, cache.getAbsolutePath()));
                mono = Mono.just(this);
            } else {
                FileOutputStream fos = new FileOutputStream(cache);
                mono = doLoad(fos, context)
                        .doOnSuccess(s -> closeStream(fos))
                        .doOnError(e -> closeStream(fos));
            }
            return mono.map(it -> {
                String contentType = getContentType();
                if (!StringUtils.hasText(contentType)) {
                    setContentType(ContentType.file(cache));
                }
                long l = cache.length();
                if (length == null) {
                    length = l;
                } else {
                    if (length != l) {
                        length = l;
                    }
                }
                inputStream = getCacheInputStream(cache);
                return it;
            });
        } else {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            return doLoad(os, context).map(it -> {
                byte[] bytes = os.toByteArray();
                long l = bytes.length;
                if (length == null) {
                    length = l;
                } else {
                    if (length != l) {
                        length = l;
                    }
                }
                inputStream = new ByteArrayInputStream(bytes);
                return it;
            });
        }
    }

    private void closeStream(OutputStream os) {
        try {
            os.close();
        } catch (IOException ignore) {
        }
    }

    @Override
    public InputStream openInputStream() {
        return inputStream;
    }

    @SneakyThrows
    public InputStream getCacheInputStream(File cache) {
        return new FileInputStream(cache);
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
    public abstract Mono<Source> doLoad(OutputStream os, DownloadContext context);

    public static abstract class Builder<T extends AbstractLoadableSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        @Override
        protected T build(T target) {
            return super.build(target);
        }
    }
}
