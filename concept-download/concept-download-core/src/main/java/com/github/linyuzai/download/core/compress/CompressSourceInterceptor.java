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
        Compressible compressibleSource;
        boolean single = source.isSingle();
        boolean forceCompress = context.getOptions().isForceCompress();
        if (single && !forceCompress) {
            compressibleSource = new Uncompressed(source);
        } else {
            String compressFormat = context.getOptions().getCompressFormat();
            String formatToUse = (compressFormat == null || compressFormat.isEmpty()) ?
                    CompressFormat.ZIP : compressFormat;
            SourceCompressor compressor = sourceCompressorAdapter.getCompressor(formatToUse, context);
            String cachePath = context.getOptions().getCompressCachePath();
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(source, null, context);
            compressibleSource = compressor.compress(source, writer, cachePath, context);
        }
        context.set(Compressible.class, compressibleSource);
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
            Compressible compressible = context.get(Compressible.class);
            if (compressible != null) {
                compressible.deleteCache();
            }
        }
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESS_SOURCE;
    }
}
