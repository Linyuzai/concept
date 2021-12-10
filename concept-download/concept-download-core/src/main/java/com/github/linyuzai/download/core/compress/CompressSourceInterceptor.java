package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.cache.DownloadCacheLocation;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
public class CompressSourceInterceptor implements DownloadInterceptor, DownloadContextInitializer, DownloadContextDestroyer {

    private SourceCompressorAdapter sourceCompressorAdapter;

    private DownloadCacheLocation cacheLocation;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        Source source = context.get(Source.class);
        Compressible compressibleSource;
        boolean single = source.isSingle();
        boolean compressOnSingle = context.getOptions().isCompressOnSingle();
        if (single && !compressOnSingle) {
            compressibleSource = new Uncompressed(source);
        } else {
            String compressFormat = context.getOptions().getCompressFormat();
            String finalFormat = (compressFormat == null || compressFormat.isEmpty()) ?
                    CompressFormat.ZIP : compressFormat;
            SourceCompressor compressor =
                    sourceCompressorAdapter.getCompressor(finalFormat, context);
            String path = cacheLocation.getPath();
            String group = context.getOptions().getCompressCacheGroup();
            File cacheDir = (group == null || group.isEmpty()) ? new File(path) : new File(path, group);
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(source, null, context);
            compressibleSource = compressor
                    .compress(source, writer, cacheDir.getAbsolutePath(), context);
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
        boolean delete = context.getOptions().isDeleteCompressCache();
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
