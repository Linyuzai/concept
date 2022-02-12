package com.github.linyuzai.download.core.handler.impl;

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
 * 压缩处理器 / A handler to process compression
 * 单个下载源默认不压缩 / A single download source is not compressed by default
 * 可以通过指定配置强制压缩 / You can force compression by specifying the configuration {@link DownloadOptions#isForceCompress()}
 * 如果需要压缩则会适配压缩器 / If compression is required, the compressor will be adapted
 * 将通过压缩器对下载源进行压缩 / The download source will be compressed by a compressor
 */
@AllArgsConstructor
public class CompressSourceHandler implements DownloadHandler, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceCompressorAdapter sourceCompressorAdapter;

    /**
     * 压缩处理 / Compression processing
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        Source source = context.get(Source.class);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        Compression compression;
        boolean single = source.isSingle();
        boolean forceCompress = context.getOptions().isForceCompress();
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
     * 初始化时，将压缩器适配器添加到下载上下文 / Add the adapter of compressor to the download context when initializing
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceCompressorAdapter.class, sourceCompressorAdapter);
    }

    /**
     * 销毁时，删除压缩缓存 / Delete the compressed cache when destroyed if necessary
     * {@link DownloadOptions#isCompressCacheDelete()}
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public void destroy(DownloadContext context) {
        Compression compression = context.get(Compression.class);
        if (compression != null) {
            DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
            boolean delete = context.getOptions().isCompressCacheDelete();
            if (delete) {
                compression.deleteCache();
                publisher.publish(new CompressionCacheDeletedEvent(context, compression));
            }
            compression.release();
            publisher.publish(new CompressionReleasedEvent(context, compression));
        }
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESS_SOURCE;
    }
}
