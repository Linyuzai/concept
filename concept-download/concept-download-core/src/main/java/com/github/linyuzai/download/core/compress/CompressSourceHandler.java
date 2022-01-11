package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.handler.AutomaticDownloadHandler;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * 压缩处理器 / A handler to process compression
 * 单个下载源默认不压缩 / A single download source is not compressed by default
 * 可以通过指定配置强制压缩 / You can force compression by specifying the configuration {@link DownloadOptions#isForceCompress()}
 * 如果需要压缩则会适配压缩器 / If compression is required, the compressor will be adapted
 * 将通过压缩器对下载源进行压缩 / The download source will be compressed by a compressor
 */
@AllArgsConstructor
public class CompressSourceHandler implements AutomaticDownloadHandler, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceCompressorAdapter sourceCompressorAdapter;

    /**
     * 压缩处理 / Compression processing
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public void doHandle(DownloadContext context) {
        Mono<Source> source = context.get(Source.class);
        Mono<Compression> compression = source.flatMap(it -> {
            boolean single = it.isSingle();
            boolean forceCompress = context.getOptions().isForceCompress();
            if (single && !forceCompress) {
                return Mono.just(new NoCompression(it));
            } else {
                String compressFormat = context.getOptions().getCompressFormat();
                String formatToUse = (compressFormat == null || compressFormat.isEmpty()) ?
                        CompressFormat.ZIP : compressFormat;
                SourceCompressor compressor = sourceCompressorAdapter.getCompressor(formatToUse, context);
                DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
                DownloadWriter writer = writerAdapter.getWriter(it, null, context);
                return compressor.compress(source, writer, context);
            }
        });

        context.set(Compression.class, compression);
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
        boolean delete = context.getOptions().isCompressCacheDelete();
        if (delete) {
            Compression compression = context.get(Compression.class);
            if (compression != null) {
                compression.deleteCache();
            }
        }
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESS_SOURCE;
    }
}
