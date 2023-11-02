package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.compress.*;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadLifecycleListener;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 对 {@link Source} 进行压缩。
 */
@Getter
@RequiredArgsConstructor
public class CompressSourceHandler implements DownloadHandler, DownloadLifecycleListener {

    /**
     * {@link SourceCompressor} 适配器
     */
    private final SourceCompressorAdapter sourceCompressorAdapter;

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
    @SneakyThrows
    @Override
    public Object handle(DownloadContext context, DownloadHandlerChain chain) {
        DownloadOptions options = context.get(DownloadOptions.class);
        Source source = context.get(Source.class);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        Compression compression;
        boolean single = source.isSingle();
        boolean forceCompress = options.isForceCompress();
        //（单一文件 && 没有强制压缩），则不压缩
        if (single && !forceCompress) {
            compression = new NoCompression(source);
            publisher.publish(new SourceNoCompressionEvent(context, source));
        } else {
            String compressFormat = options.getCompressFormat();
            String formatToUse = (compressFormat == null || compressFormat.isEmpty()) ?
                    CompressFormat.ZIP : compressFormat;
            SourceCompressor compressor = sourceCompressorAdapter.getCompressor(formatToUse, context);
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(source, context);
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
    public void onStart(DownloadContext context) {
        context.set(SourceCompressorAdapter.class, sourceCompressorAdapter);
    }

    /**
     * 销毁时，如果需要则删除缓存并发布 {@link CompressionCacheDeletedEvent} 事件；
     * 释放资源并发布 {@link CompressionReleasedEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void onComplete(DownloadContext context) {
        Compression compression = context.get(Compression.class);
        if (compression != null) {
            DownloadOptions options = context.get(DownloadOptions.class);
            DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
            boolean delete = options.isCompressCacheDelete();
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
