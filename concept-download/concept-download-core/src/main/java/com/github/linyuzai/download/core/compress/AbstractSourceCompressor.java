package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.compress.zip.SourceZipCompressedEvent;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.Progress;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 抽象的Source压缩器 / Abstract class of source compressor
 * 进行了统一的缓存处理 / Unified cache processing
 */
public abstract class AbstractSourceCompressor implements SourceCompressor {

    /**
     * 如果没有启用缓存，使用内存压缩 / Use memory compression if caching is not enabled
     * 内存压缩会将压缩操作延后到写入响应时触发 / Memory compression delays the compression operation when writing response
     * 如果启用缓存并且缓存存在，直接使用缓存 / Use the cache directly if caching is enabled and the cache exists
     * 如果启用缓存并且缓存不存在，压缩到本地缓存文件 / Compress to the local cache file if caching is enabled and the cache does not exist
     *
     * @param source  {@link Source}
     * @param writer  {@link DownloadWriter}
     * @param context 下载上下文 / Context of download
     * @return An specific compression
     */
    @SneakyThrows
    @Override
    public Compression compress(Source source, DownloadWriter writer, DownloadContext context) {
        String cachePath = context.getOptions().getCompressCachePath();
        String cacheName = getCacheName(source, context);
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        if (cacheEnable) {
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File cache = new File(dir, cacheName);
            if (cache.exists()) {
                publisher.publish(new SourceCompressedCacheUsedEvent(context, source, cache.getAbsolutePath()));
            } else {
                try (FileOutputStream fos = new FileOutputStream(cache)) {
                    doCompress(source, fos, writer, context);
                }
                publisher.publish(new SourceFileCompressedEvent(context, source, cache.getAbsolutePath()));
            }
            FileCompression compression = new FileCompression(cache);
            compression.setContentType(getContentType());
            return compression;
        } else {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            doCompress(source, os, writer, context);
            MemoryCompression compression = new MemoryCompression(os.toByteArray());
            compression.setName(cacheName);
            compression.setContentType(getContentType());
            publisher.publish(new SourceMemoryCompressedEvent(context, source));
            return compression;
        }
    }

    /**
     * 执行压缩 / Perform compression
     *
     * @param source 被压缩的对象 / Object to compress
     * @param os     写入的输出流 / Output stream to write
     * @param writer 写入执行器 / Executor of writing
     */
    @SneakyThrows
    public void doCompress(Source source, OutputStream os, DownloadWriter writer, DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        try (OutputStream nos = newOutputStream(os, source)) {
            Progress progress = new Progress(source.getLength());
            Collection<Part> parts = source.getParts();
            for (Part part : parts) {
                InputStream is = part.getInputStream();
                beforeWrite(part, nos);
                writer.write(is, nos, null, part.getCharset(), part.getLength(), (current, increase) -> {
                    progress.update(increase);
                    publisher.publish(new SourceCompressingProgressEvent(context, progress.copy()));
                });
                afterWrite(part, nos);
            }
        }
        publisher.publish(new SourceZipCompressedEvent(context, source));
    }

    public abstract OutputStream newOutputStream(OutputStream os, Source source);

    public abstract void beforeWrite(Part part, OutputStream os);

    public abstract void afterWrite(Part part, OutputStream os);

    /**
     * 如果指定了缓存名称则使用指定的名称 / If a cache name is specified, the specified name is used
     * 否则使用Source的名称 / Otherwise, use the name of the source {@link Source#getName()}
     * 如果对应的名称为空 / If the name is empty
     * 否则使用缓存名称生成器生成 / Otherwise, it is generated using the cache name generator
     *
     * @param source  被压缩的对象 / Object to compress
     * @param context 下载上下文 / Context of download
     * @return 缓存名称 / Name of cache
     * @see CacheNameGenerator
     */
    public String getCacheName(Source source, DownloadContext context) {
        String compressCacheName = context.getOptions().getCompressCacheName();
        String suffix = getSuffix();
        String nameToUse;
        if (StringUtils.hasText(compressCacheName)) {
            nameToUse = compressCacheName;
        } else {
            CacheNameGenerator generator = context.get(CacheNameGenerator.class);
            nameToUse = generator.generate(source, context);
        }
        if (!StringUtils.hasText(nameToUse)) {
            throw new DownloadException("Cache name is null or empty");
        }
        if (nameToUse.endsWith(suffix)) {
            return nameToUse;
        }
        int index = nameToUse.lastIndexOf(CompressFormat.DOT);
        if (index == -1) {
            return nameToUse + suffix;
        } else {
            return nameToUse.substring(0, index) + suffix;
        }
    }

    /**
     * @return 压缩包的后缀 / Suffix of the compressed package
     */
    public abstract String getSuffix();

    /**
     * @return Content Type
     */
    public abstract String getContentType();
}
