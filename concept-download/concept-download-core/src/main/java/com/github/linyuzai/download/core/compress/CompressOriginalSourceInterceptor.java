package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.cache.DownloadCacheLocation;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.SourceWriter;
import com.github.linyuzai.download.core.writer.SourceWriterAdapter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@AllArgsConstructor
public class CompressOriginalSourceInterceptor implements DownloadInterceptor {

    private DownloadCacheLocation cacheLocation;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        OriginalSource source = context.get(OriginalSource.class);
        context.set(Source.class, source);
        boolean compressEnabled = context.getOptions().isCompressEnabled();
        Collection<OriginalSource> sources = source.flatten();
        if (compressEnabled) {
            boolean skipCompressOnSingleSource = context.getOptions().isSkipCompressOnSingle();
            if (sources.size() != 1 || !skipCompressOnSingleSource) {
                String compressFormat = context.getOptions().getCompressFormat();
                String finalFormat = (compressFormat == null || compressFormat.isEmpty()) ?
                        CompressFormat.ZIP : compressFormat;
                OriginalSourceCompressorAdapter compressorAdapter =
                        context.get(OriginalSourceCompressorAdapter.class);
                OriginalSourceCompressor compressor =
                        compressorAdapter.getOriginalSourceCompressor(finalFormat, context);
                String path = cacheLocation.getPath();
                String group = context.getOptions().getCompressCacheGroup();
                File dir = (group == null || group.isEmpty()) ? new File(path) : new File(path, group);
                boolean mkdirs = dir.mkdirs();
                SourceWriterAdapter writerAdapter = context.get(SourceWriterAdapter.class);
                SourceWriter writer = writerAdapter.getSourceWriter(source, null, context);
                boolean delete = context.getOptions().isDeleteCompressCache();
                CompressedSource compressedSource = compressor
                        .compress(source, writer, dir.getAbsolutePath(), delete, context);
                context.set(Source.class, compressedSource);
            }
        } else {
            if (sources.size() > 1) {
                throw new OriginalSourceCompressException("Enable compress to download multi-source");
            }
        }
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESS_SOURCE;
    }
}
