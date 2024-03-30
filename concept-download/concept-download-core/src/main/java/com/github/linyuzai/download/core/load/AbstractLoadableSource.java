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

import java.io.*;
import java.nio.file.Files;

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
    public void load(DownloadContext context) {
        DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        if (inputStream != null) {
            //直接使用
            publisher.publish(new SourceAlreadyLoadedEvent(context, this));
            return;
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
            if (nameToUse == null || nameToUse.isEmpty()) {
                throw new DownloadException("Cache name is null or empty");
            }

            File cache = new File(dir, nameToUse);
            //缓存存在
            if (cache.exists()) {
                publisher.publish(new SourceLoadedUsingCacheEvent(context, this, cache.getAbsolutePath()));
            } else {
                //写到缓存文件
                try (FileOutputStream fos = new FileOutputStream(cache)) {
                    doLoad(fos, context);
                } catch (Throwable e) {
                    if (cache.exists()) {
                        boolean delete = cache.delete();
                    }
                    throw e;
                }
            }

            //Content Type
            String contentType = getContentType();
            if (contentType == null || contentType.isEmpty()) {
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
        } else {
            //内存加载
            byte[] bytes;
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                doLoad(os, context);
                bytes = os.toByteArray();
            }
            long l = bytes.length;
            if (length == null) {
                length = l;
            } else {
                if (length != l) {
                    length = l;
                }
            }
            inputStream = new ByteArrayInputStream(bytes);
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
    public InputStream getCacheInputStream(File cache) throws IOException {
        return Files.newInputStream(cache.toPath());
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
     */
    public abstract void doLoad(OutputStream os, DownloadContext context) throws IOException;

    public static abstract class Builder<T extends AbstractLoadableSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        @Override
        protected T build(T target) {
            return super.build(target);
        }
    }
}
