package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.aop.annotation.Download;
import com.github.linyuzai.download.core.compress.*;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * 对 {@link Source} 进行压缩。
 * 压缩处理器 / A handler to process compression
 * 单个下载源默认不压缩 / A single download source is not compressed by default
 * 可以通过指定配置强制压缩 / You can force compression by specifying the configuration {@link DownloadOptions#isForceCompress()}
 * 如果需要压缩则会适配压缩器 / If compression is required, the compressor will be adapted
 * 将通过压缩器对下载源进行压缩 / The download source will be compressed by a compressor
 */
@AllArgsConstructor
public class CompressSourceHandler implements DownloadHandler, DownloadContextInitializer, DownloadContextDestroyer {

    /**
     * {@link SourceCompressor} 适配器。
     */
    private SourceCompressorAdapter sourceCompressorAdapter;

    /**
     * 压缩 {@link Source}。
     * 默认单一文件时不压缩并发布 {@link SourceNoCompressionEvent} 事件，
     * 可通过 {@link Download#forceCompress()} 强制压缩。
     * 根据指定或默认的压缩格式，通过 {@link SourceCompressorAdapter} 获得 {@link SourceCompressor}，
     * 通过 {@link SourceCompressor} 执行压缩。
     * 发布 {@link AfterSourceCompressedEvent} 事件，
     * 将压缩后的 {@link Compression} 设置到 {@link DownloadContext} 中。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        Source source = context.get(Source.class);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        Compression compression;
        boolean single = source.isSingle();
        boolean forceCompress = context.getOptions().isForceCompress();
        //（单一文件 && 没有强制压缩），则不压缩
        if (single && !forceCompress) {
            compression = new NoCompression(source);
            publisher.publish(new SourceNoCompressionEvent(context, source));
        } else {
            String compressFormat = context.getOptions().getCompressFormat();
            String formatToUse = StringUtils.hasText(compressFormat) ? compressFormat : CompressFormat.ZIP;
            SourceCompressor compressor = sourceCompressorAdapter.getCompressor(formatToUse, context);
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(source, null, context);
            compression = compressor.compress(source, writer, context);
        }
        publisher.publish(new AfterSourceCompressedEvent(context, source, compression));
        context.set(Compression.class, compression);
        return chain.next(context);
    }

    /**
     * 初始化时，将 {@link SourceCompressorAdapter} 设置到 {@link DownloadContext}。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceCompressorAdapter.class, sourceCompressorAdapter);
    }

    /**
     * 销毁时，如果需要则删除缓存并发布 {@link CompressionCacheDeletedEvent} 事件；
     * 释放资源并发布 {@link CompressionReleasedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void destroy(DownloadContext context) {
        Compression compression = context.get(Compression.class);
        if (compression != null) {
            DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
            boolean delete = context.getOptions().isCompressCacheDelete();
            //是否删除缓存
            if (delete) {
                compression.deleteCache();
                publisher.publish(new CompressionCacheDeletedEvent(context, compression));
            }
            //释放资源
            compression.release();
            publisher.publish(new CompressionReleasedEvent(context, compression));
        }
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESS_SOURCE;
    }
}
