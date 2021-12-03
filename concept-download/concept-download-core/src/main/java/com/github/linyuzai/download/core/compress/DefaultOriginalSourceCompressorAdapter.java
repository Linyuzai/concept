package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.exception.DownloadException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DefaultOriginalSourceCompressorAdapter implements OriginalSourceCompressorAdapter, DownloadContextInitializer {

    private List<OriginalSourceCompressor> compressors;

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
