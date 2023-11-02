package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.Progress;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Collection;

/**
 * {@link SourceCompressor} 的抽象类，对缓存做了处理。
 */
public abstract class AbstractSourceCompressor<OS extends OutputStream> implements SourceCompressor {

    /**
     * 如果没有启用缓存，使用内存压缩；
     * 如果启用缓存并且缓存存在，直接使用缓存；
     * 如果启用缓存并且缓存不存在，压缩到本地缓存文件。
     *
     * @param source  {@link Source}
     * @param writer  {@link DownloadWriter}
     * @param context {@link DownloadContext}
     * @return {@link MemoryCompression} / {@link FileCompression}
     */
    @Override
    public Compression compress(Source source, DownloadWriter writer, DownloadContext context) throws IOException {
        String cachePath = context.getOptions().getCompressCachePath();
        String cacheName = getCacheName(source, context);
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        //是否启用缓存
        if (cacheEnable) {
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File cache = new File(dir, cacheName);
            //缓存是否存在
            if (cache.exists()) {
                publisher.publish(new SourceCompressedCacheUsedEvent(context, source, cache.getAbsolutePath()));
            } else {
                publisher.publish(new SourceFileCompressionEvent(context, source, cache));
                //写入缓存文件
                try (FileOutputStream fos = new FileOutputStream(cache)) {
                    doCompress(source, fos, writer, context);
                }
            }
            FileCompression compression = new FileCompression(cache);
            compression.setContentType(getContentType());
            return compression;
        } else {
            //在内存中压缩
            publisher.publish(new SourceMemoryCompressionEvent(context, source));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            doCompress(source, os, writer, context);
            MemoryCompression compression = new MemoryCompression(os.toByteArray());
            compression.setName(cacheName);
            compression.setContentType(getContentType());
            return compression;
        }
    }

    /**
     * 执行压缩。
     *
     * @param source {@link Source}
     * @param os     {@link OutputStream}
     * @param writer {@link DownloadWriter}
     */
    @SneakyThrows
    public void doCompress(Source source, OutputStream os, DownloadWriter writer, DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new SourceCompressionFormatEvent(context, source, getFormat()));
        try (OS nos = newOutputStream(os, source, context)) {
            Progress progress = new Progress(source.getLength());
            Collection<Part> parts = source.getParts();
            for (Part part : parts) {
                InputStream is = part.getInputStream();
                beforeWrite(part, nos);
                writer.write(is, nos, null, part.getCharset(), part.getLength(), (current, increase) -> {
                    progress.update(increase);
                    publisher.publish(new SourceCompressingProgressEvent(context, progress.freeze()));
                });
                afterWrite(part, nos);
            }
        }
    }

    /**
     * 新建一个压缩输出流。
     *
     * @param os      被包装的输出流
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 新建的压缩输出流
     */
    public abstract OS newOutputStream(OutputStream os, Source source, DownloadContext context);

    /**
     * 写入之前调用。
     *
     * @param part {@link Part}
     * @param os   {@link OS}
     */
    public abstract void beforeWrite(Part part, OS os);

    /**
     * 写入之后调用。
     *
     * @param part {@link Part}
     * @param os   {@link OS}
     */
    public abstract void afterWrite(Part part, OS os);

    /**
     * 如果指定了压缩文件缓存名称则使用指定的名称，
     * 否则通过 {@link CacheNameGenerator} 生成。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 压缩文件缓存名称
     */
    public String getCacheName(Source source, DownloadContext context) {
        String compressCacheName = context.getOptions().getCompressCacheName();
        String suffix = getSuffix();
        String nameToUse;
        if (compressCacheName == null || compressCacheName.isEmpty()) {
            CacheNameGenerator generator = context.get(CacheNameGenerator.class);
            nameToUse = generator.generate(source, context);
        } else {
            nameToUse = compressCacheName;
        }
        if (nameToUse == null || nameToUse.isEmpty()) {
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
     * 获得压缩文件的扩展后缀。
     *
     * @return 后缀
     */
    public abstract String getSuffix();

    /**
     * 获得压缩文件的 Content-Type
     *
     * @return Content-Type
     */
    public abstract String getContentType();
}
