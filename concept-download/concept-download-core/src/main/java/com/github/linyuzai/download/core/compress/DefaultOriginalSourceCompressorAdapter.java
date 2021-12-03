package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.order.OrderProvider;

import java.util.Comparator;
import java.util.List;

public class DefaultOriginalSourceCompressorAdapter implements OriginalSourceCompressorAdapter, DownloadContextInitializer {

    private final List<OriginalSourceCompressor> compressors;

    public DefaultOriginalSourceCompressorAdapter(List<OriginalSourceCompressor> compressors) {
        this.compressors = compressors;
        this.compressors.sort(Comparator.comparingInt(OrderProvider::getOrder));
    }

    @Override
    public OriginalSourceCompressor getOriginalSourceCompressor(String format, DownloadContext context) {
        for (OriginalSourceCompressor compressor : compressors) {
            if (compressor.support(format, context)) {
                return compressor;
            }
        }
        throw new DownloadException("No OriginalSourceCompressor support: " + format);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(OriginalSourceCompressorAdapter.class, this);
    }
}
