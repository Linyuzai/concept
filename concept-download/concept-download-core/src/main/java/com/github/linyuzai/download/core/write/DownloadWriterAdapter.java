package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.web.Range;

/**
 * 写入器的适配器 / Adapter of write
 */
public interface DownloadWriterAdapter {

    /**
     * 匹配一个写入器 / Match a write
     *
     * @param downloadable 可下载的资源 / Resource can be downloaded
     * @param range        写入的范围 / Range of writing
     * @param context      下载上下文 / Context of download
     * @return 匹配到的写入器 / Writer matched
     */
    DownloadWriter getWriter(Downloadable downloadable, Range range, DownloadContext context);
}
