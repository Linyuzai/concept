package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.cache.CacheNameGenerator;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadMethod;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

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
     * @throws IOException I/O exception
     */
    @Override
    public Compression compress(Source source, DownloadWriter writer, DownloadContext context) throws IOException {
        Compression result;
        String cachePath = context.getOptions().getCompressCachePath();
        String cacheName = getCacheName(source, context);
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        if (cacheEnable) {
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File cache = new File(dir, cacheName);
            if (!cache.exists()) {
                FileOutputStream fos = new FileOutputStream(cache);
                doCompress(source, fos, writer);
            }
            result = new FileCompression(cache);
        } else {
            MemoryCompression compressed = new MemoryCompression(source, writer, this);
            compressed.setName(cacheName);
            result = compressed;
        }
        return result;
    }

    /**
     * 执行压缩 / Perform compression
     *
     * @param source 被压缩的对象 / Object to compress
     * @param os     写入的输出流 / Output stream to write
     * @param writer 写入执行器 / Executor of writing
     * @throws IOException I/O exception
     */
    public abstract void doCompress(Source source, OutputStream os, DownloadWriter writer) throws IOException;

    /**
     * 如果指定了缓存名称则使用指定的名称 / If a cache name is specified, the specified name is used
     * 否则使用Source的名称 / Otherwise, use the name of the source {@link Source#getName()}
     * 如果对应的名称为空 / If the name is empty
     * 否则使用缓存名称生成器生成，默认使用时间戳 / Otherwise, it is generated using the cache name generator, and the timestamp is used by default
     *
     * @param source  被压缩的对象 / Object to compress
     * @param context 下载上下文 / Context of download
     * @return 缓存名称 / Name of cache
     */
    public String getCacheName(Source source, DownloadContext context) {
        String cacheName = context.getOptions().getCompressCacheName();
        String suffix = getSuffix();
        if (cacheName == null || cacheName.isEmpty()) {
            CacheNameGenerator generator = context.get(CacheNameGenerator.class);
            String newCacheName = generator.generate(source, context);
            if (newCacheName == null || newCacheName.isEmpty()) {
                throw new DownloadException("Cache name is null or empty");
            } else {
                int index = newCacheName.lastIndexOf(CompressFormat.DOT);
                if (index == -1) {
                    return newCacheName + suffix;
                } else {
                    return newCacheName.substring(0, index) + suffix;
                }
            }
        } else {
            if (cacheName.endsWith(suffix)) {
                return cacheName;
            } else {
                return cacheName + suffix;
            }
        }
    }

    /**
     * @return 压缩包的后缀 / Suffix of the compressed package
     */
    public abstract String getSuffix();
}
