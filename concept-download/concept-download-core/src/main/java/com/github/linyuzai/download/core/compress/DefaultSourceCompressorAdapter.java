package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * {@link SourceCompressorAdapter} 的默认实现。
 */
@Getter
@RequiredArgsConstructor
public class DefaultSourceCompressorAdapter implements SourceCompressorAdapter {

    /**
     * {@link SourceCompressor} 列表
     */
    private final List<SourceCompressor> compressors;

    /**
     * 根据压缩格式获得对应的 {@link SourceCompressor}，
     * 如果没有可用的 {@link SourceCompressor} 则抛出异常。
     *
     * @param format  压缩格式
     * @param context {@link DownloadContext}
     * @return 匹配上的 {@link SourceCompressor}
     */
    @Override
    public SourceCompressor getCompressor(String format, DownloadContext context) {
        for (SourceCompressor compressor : compressors) {
            if (compressor.support(format, context)) {
                return compressor;
            }
        }
        DownloadOptions options = DownloadOptions.get(context);
        String password = options.getCompressPassword();
        if (password == null || password.isEmpty()) {
            throw new DownloadException("No SourceCompressor support: " + format);
        } else {
            throw new DownloadException("No SourceCompressor support with encryption: " + format);
        }
    }
}
