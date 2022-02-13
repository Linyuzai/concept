package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.concept.DownloadableResource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.web.Range;

/**
 * {@link DownloadWriter} 的适配器。
 */
public interface DownloadWriterAdapter {

    /**
     * 匹配一个 {@link DownloadWriter}。
     *
     * @param downloadableResource {@link DownloadableResource}
     * @param range                {@link Range}
     * @param context              {@link DownloadContext}
     * @return 匹配到的 {@link DownloadWriter}
     */
    DownloadWriter getWriter(DownloadableResource downloadableResource, Range range, DownloadContext context);
}
