package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * {@link SourceLoader} 的默认实现。
 */
public class DefaultSourceLoader implements SourceLoader {

    /**
     * 直接调用 {@link Source#load(DownloadContext)}。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     */
    @Override
    public void load(Source source, DownloadContext context) {
        source.load(context);
    }
}
