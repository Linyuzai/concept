package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.cache.DownloadCacheLocation;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextDestroyer;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import com.github.linyuzai.download.core.writer.SourceWriterAdapter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
public class CompressOriginalSourceInterceptor implements DownloadInterceptor, DownloadContextInitializer, DownloadContextDestroyer {

    private OriginalSourceCompressorAdapter originalSourceCompressorAdapter;

    private DownloadCacheLocation cacheLocation;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        OriginalSource source = context.get(OriginalSource.class);
        CompressedSource compressedSource;
        boolean single = source.isSingle();
        boolean compressOnSingle = context.getOptions().isCompressOnSingle();
        if (single && !compressOnSingle) {
            compressedSource = new UncompressedSource(source);
        } else {
            String compressFormat = context.getOptions().getCompressFormat();
            String finalFormat = (compressFormat == null || compressFormat.isEmpty()) ?
                    CompressFormat.ZIP : compressFormat;
            OriginalSourceCompressor compressor =
                    originalSourceCompressorAdapter.getOriginalSourceCompressor(finalFormat, context);
            String path = cacheLocation.getPath();
            String group = context.getOptions().getCompressCacheGroup();
            File cacheDir = (group == null || group.isEmpty()) ? new File(path) : new File(path, group);
            SourceWriterAdapter writerAdapter = context.get(SourceWriterAdapter.class);
            SourceWriter writer = writerAdapter.getSourceWriter(source, null, context);
            compressedSource = compressor
                    .compress(source, writer, cacheDir.getAbsolutePath(), context);
        }
        context.set(CompressedSource.class, compressedSource);
        chain.next(context);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(OriginalSourceCompressorAdapter.class, originalSourceCompressorAdapter);
    }

    @Override
    public void destroy(DownloadContext context) {
        boolean delete = context.getOptions().isDeleteCompressCache();
        if (delete) {
            CompressedSource source = context.get(CompressedSource.class);
            if (source != null) {
                source.deleteCache();
            }
        }
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESS_SOURCE;
    }
}
