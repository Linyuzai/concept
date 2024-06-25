package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.Progress;

import java.io.*;
import java.util.Collection;

/**
 * {@link SourceCompressor} 的抽象类，对缓存做了处理。
 */
public abstract class AbstractSourceCompressor<OS extends OutputStream> implements SourceCompressor {

    @Override
    public boolean support(String format, DownloadContext context) {
        for (String supported : getFormats()) {
            if (format.equalsIgnoreCase(supported)) {
                if (supportEncryption(context)) {
                    return true;
                } else {
                    DownloadOptions options = DownloadOptions.get(context);
                    String password = options.getCompressPassword();
                    if (password == null || password.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获得压缩格式。
     *
     * @return 压缩格式
     */
    public abstract String[] getFormats();

    /**
     * 如果没有启用缓存，使用内存压缩；
     * 如果启用缓存并且缓存存在，直接使用缓存；
     * 如果启用缓存并且缓存不存在，压缩到本地缓存文件。
     *
     * @param source  {@link Source}
     * @param format  压缩格式
     * @param writer  {@link DownloadWriter}
     * @param context {@link DownloadContext}
     * @return {@link MemoryCompression} / {@link FileCompression}
     */
    @Override
    public Compression compress(Source source, String format, DownloadWriter writer, DownloadContext context) throws IOException {
        DownloadOptions options = DownloadOptions.get(context);
        String cachePath = options.getCompressCachePath();
        String cacheName = getCacheName(source, format, context);
        boolean cacheEnable = options.isCompressCacheEnabled();
        DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        //是否启用缓存
        if (cacheEnable) {
            File dir = new File(cachePath);
            /*if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }*/
            File cache = new File(dir, cacheName);

            File parent = cache.getParentFile();
            if (parent != null && !parent.exists()) {
                boolean mkdirs = parent.mkdirs();
            }

            //缓存是否存在
            if (cache.exists()) {
                publisher.publish(new SourceCompressedUsingCacheEvent(context, source, cache.getAbsolutePath()));
            } else {
                publisher.publish(new SourceFileCompressionEvent(context, source, cache));
                //写入缓存文件
                try (FileOutputStream fos = new FileOutputStream(cache);
                     OutputStream wrapper = wrapper(fos)) {
                    doCompress(source, wrapper, writer, context);
                } catch (Throwable e) {
                    if (cache.exists()) {
                        boolean delete = cache.delete();
                    }
                    throw e;
                }
            }
            FileCompression compression = new FileCompression(cache);
            compression.setContentType(getContentType(format));
            return compression;
        } else {
            //在内存中压缩
            publisher.publish(new SourceMemoryCompressionEvent(context, source));
            MemoryCompression compression;
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 OutputStream wrapper = wrapper(os)) {
                doCompress(source, wrapper, writer, context);
                compression = new MemoryCompression(os.toByteArray());
            }
            compression.setName(cacheName);
            compression.setContentType(getContentType(format));
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
    public void doCompress(Source source, OutputStream os, DownloadWriter writer, DownloadContext context) throws IOException {
        DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        DownloadOptions options = DownloadOptions.get(context);
        String format = options.getCompressFormat();
        publisher.publish(new SourceCompressionFormatEvent(context, source, format));
        try (OS nos = newOutputStream(os, source, format, context)) {
            Progress progress = new Progress(source.getLength());
            Collection<Part> parts = source.getParts();
            for (Part part : parts) {
                InputStream is = part.getInputStream();
                beforeWrite(part, nos, context);
                writer.write(is, nos, null, part.getCharset(), part.getLength(), (current, increase) -> {
                    progress.update(increase);
                    publisher.publish(new SourceCompressingProgressEvent(context, progress.freeze()));
                });
                afterWrite(part, nos, context);
            }
        }
    }

    protected OutputStream wrapper(OutputStream os) throws IOException {
        return os;
    }

    /**
     * 是否支持加密
     */
    public abstract boolean supportEncryption(DownloadContext context);

    /**
     * 新建一个压缩输出流。
     *
     * @param os      被包装的输出流
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 新建的压缩输出流
     */
    public abstract OS newOutputStream(OutputStream os, Source source, String format, DownloadContext context) throws IOException;

    /**
     * 写入之前调用。
     *
     * @param part {@link Part}
     * @param os   {@link OS}
     */
    public abstract void beforeWrite(Part part, OS os, DownloadContext context) throws IOException;

    /**
     * 写入之后调用。
     *
     * @param part {@link Part}
     * @param os   {@link OS}
     */
    public abstract void afterWrite(Part part, OS os, DownloadContext context) throws IOException;

    /**
     * 如果指定了压缩文件缓存名称则使用指定的名称，
     * 否则通过 {@link CacheNameGenerator} 生成。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 压缩文件缓存名称
     */
    public String getCacheName(Source source, String format, DownloadContext context) {
        DownloadOptions options = DownloadOptions.get(context);
        String compressCacheName = options.getCompressCacheName();
        String suffix = getSuffix(format);
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
    public abstract String getSuffix(String format);

    /**
     * 获得压缩文件的 Content-Type
     *
     * @return Content-Type
     */
    public abstract String getContentType(String format);
}
