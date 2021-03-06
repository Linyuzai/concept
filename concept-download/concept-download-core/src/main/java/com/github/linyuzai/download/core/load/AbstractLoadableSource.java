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
 * 支持预加载的 {@link Source}。
 */
@Getter
public abstract class AbstractLoadableSource extends AbstractSource {

    /**
     * 资源长度
     */
    protected Long length;

    /**
     * 在启用缓存并存在缓存的情况下直接同步加载本地缓存。
     *
     * @return 是否异步加载
     */
    public boolean isAsyncLoad() {
        boolean asyncLoad = super.isAsyncLoad();
        if (asyncLoad && isCacheEnabled() && isCacheExisted()) {
            return false;
        }
        return asyncLoad;
    }

    /**
     * 本地文件是否存在。
     *
     * @return 如果本地文件存在则返回 true
     */
    @Override
    public boolean isCacheExisted() {
        return new File(getCachePath(), getName()).exists();
    }

    /**
     * 如果当前已经持有 {@link InputStream} 则直接使用；
     * 不启用缓存，则直接加载到内存；
     * 启用缓存并缓存存在，则使用缓存；
     * 启用缓存并缓存不存在，则加载到缓存并使用缓存。
     *
     * @param context {@link DownloadContext}
     */
    @SneakyThrows
    @Override
    public Mono<Source> load(DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        if (inputStream != null) {
            //直接使用
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
            //缓存存在
            if (cache.exists()) {
                publisher.publish(new SourceLoadedCacheUsedEvent(context, this, cache.getAbsolutePath()));
                mono = Mono.just(this);
            } else {
                //写到缓存文件
                FileOutputStream fos = new FileOutputStream(cache);
                mono = doLoad(fos, context)
                        .doOnSuccess(s -> closeStream(fos))
                        .doOnError(e -> closeStream(fos));
            }
            return mono.map(it -> {
                //Content Type
                String contentType = getContentType();
                if (!StringUtils.hasText(contentType)) {
                    setContentType(ContentType.file(cache));
                }
                //设置长度
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
            //内存加载
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

    /**
     * 关闭流。
     *
     * @param os {@link OutputStream}
     */
    private void closeStream(OutputStream os) {
        try {
            os.close();
        } catch (IOException ignore) {
        }
    }

    /**
     * 预加载时获得。
     *
     * @return 持有的 {@link InputStream}
     */
    @Override
    public InputStream openInputStream() {
        return inputStream;
    }

    /**
     * 通过缓存文件获得 {@link FileInputStream}。
     *
     * @param cache 缓存文件
     * @return 缓存文件的 {@link FileInputStream}
     */
    @SneakyThrows
    public InputStream getCacheInputStream(File cache) {
        return new FileInputStream(cache);
    }

    /**
     * 如果缓存文件存在则删除。
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
     * 加载到对应的输出流。
     *
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    public abstract Mono<Source> doLoad(OutputStream os, DownloadContext context);

    public static abstract class Builder<T extends AbstractLoadableSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        @Override
        protected T build(T target) {
            return super.build(target);
        }
    }
}
