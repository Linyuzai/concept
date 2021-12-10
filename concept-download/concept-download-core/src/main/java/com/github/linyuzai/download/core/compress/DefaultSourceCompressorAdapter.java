package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.order.OrderProvider;

import java.util.Comparator;
import java.util.List;

public class DefaultSourceCompressorAdapter implements SourceCompressorAdapter {

    private final List<SourceCompressor> compressors;

    public DefaultSourceCompressorAdapter(List<SourceCompressor> compressors) {
        this.compressors = compressors;
        this.compressors.sort(Comparator.comparingInt(OrderProvider::getOrder));
    }

    @Override
    public SourceCompressor getCompressor(String format, DownloadContext context) {
        for (SourceCompressor compressor : compressors) {
            if (compressor.support(format, context)) {
                return compressor;
            }
        }
        throw new DownloadException("No SourceCompressor support: " + format);
    }
}
