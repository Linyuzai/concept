package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.compress.SourceCompressor;
import com.github.linyuzai.download.core.source.DownloadSource;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ZipSourceCompressor implements SourceCompressor {

    private String cachePath;

    @Override
    public boolean support(String format, DownloadContext context) {
        return CompressFormat.ZIP.equals(format);
    }

    @Override
    public CompressedSource compress(DownloadSource source, DownloadContext context) {
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        return new ZipCompressedSource(source, cacheEnable, cachePath);
    }
}
