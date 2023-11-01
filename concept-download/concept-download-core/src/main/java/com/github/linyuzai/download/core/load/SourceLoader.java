package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * {@link Source} 加载器。
 *
 * @see DefaultSourceLoader
 */
public interface SourceLoader {

    /**
     * 执行加载。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     */
    void load(Source source, DownloadContext context);
}
