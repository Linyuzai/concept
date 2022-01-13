package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * 持有下载操作参数的下载上下文 / Context of download holding download options
 */
@Getter
@AllArgsConstructor
public abstract class AbstractDownloadContext implements DownloadContext {

    @NonNull
    private final DownloadOptions options;
}
