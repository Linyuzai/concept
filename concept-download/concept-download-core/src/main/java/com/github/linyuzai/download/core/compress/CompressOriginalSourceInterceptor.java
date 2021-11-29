package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CompressOriginalSourceInterceptor implements DownloadInterceptor {

    private List<OriginalSourceCompressor> compressors;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        OriginalSource source = context.get(OriginalSource.class);
        boolean skipCompressOnSingleSource = context.getOptions().isSkipCompressOnSingleSource();
        boolean compressEnabled = context.getOptions().isCompressEnabled();
        Collection<OriginalSource> sources = source.flatten();
        boolean shouldCompress = true;
        if (compressEnabled) {
            if (sources.size() <= 1) {
                if (skipCompressOnSingleSource) {
                    shouldCompress = false;
                }
            }
        } else {
            if (sources.size() > 1) {
                throw new OriginalSourceCompressException("Enable compress to download multi-source");
            } else {
                shouldCompress = false;
            }
        }
        SourceWriter writer = context.get(SourceWriter.class);
        CompressedSource compressedSource;
        if (shouldCompress) {
            String compressFormat = context.getOptions().getCompressFormat();
            String finalFormat = (compressFormat == null || compressFormat.isEmpty()) ?
                    CompressFormat.ZIP : compressFormat;
            OriginalSourceCompressor compressor = getSourceCompressor(finalFormat, context);
            if (compressor == null) {
                throw new OriginalSourceCompressException("No SourceCompressor support: " + finalFormat);
            }
            compressedSource = compressor.compress(source, context);
        } else {
            compressedSource = new UncompressedSource(sources.iterator().next(), writer);
        }
        context.set(CompressedSource.class, compressedSource);
        chain.next(context);
    }

    public OriginalSourceCompressor getSourceCompressor(String format, DownloadContext context) {
        for (OriginalSourceCompressor compressor : compressors) {
            if (compressor.support(format, context)) {
                return compressor;
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return ORDER_COMPRESS_SOURCE;
    }
}
