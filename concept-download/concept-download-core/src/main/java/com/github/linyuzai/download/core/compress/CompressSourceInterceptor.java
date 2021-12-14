package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class CompressSourceInterceptor implements DownloadInterceptor, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceCompressorAdapter sourceCompressorAdapter;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        Source source = context.get(Source.class);
        Compression compression;
        boolean single = source.isSingle();
        boolean forceCompress = context.getOptions().isForceCompress();
        if (single && !forceCompress) {
            compression = new NoCompression(source);
        } else {
            String compressFormat = context.getOptions().getCompressFormat();
            String formatToUse = (compressFormat == null || compressFormat.isEmpty()) ?
                    CompressFormat.ZIP : compressFormat;
            SourceCompressor compressor = sourceCompressorAdapter.getCompressor(formatToUse, context);
            String cachePath = context.getOptions().getCompressCachePath();
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(source, null, context);
            compression = compressor.compress(source, writer, cachePath, context);
        }
        context.set(Compression.class, compression);
        chain.next(context);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceCompressorAdapter.class, sourceCompressorAdapter);
    }

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
