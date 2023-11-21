package com.github.linyuzai.download.core.logger;

import com.github.linyuzai.download.core.compress.AbstractCompressSourceEvent;
import com.github.linyuzai.download.core.compress.CompressionCacheDeletedEvent;
import com.github.linyuzai.download.core.compress.CompressionReleasedEvent;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.event.DownloadStartedEvent;
import com.github.linyuzai.download.core.load.AbstractLoadSourceEvent;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceCacheDeletedEvent;
import com.github.linyuzai.download.core.source.SourceReleasedEvent;
import com.github.linyuzai.download.core.web.AbstractWriteResponseEvent;

/**
 * 标准流程日志。
 * 每个流程相关的事件都会打印。
 */
public class StandardDownloadLogger extends LoggingDownloadEventListener {

    /**
     * 包括 {@link DownloadContext} 初始化和销毁，
     * {@link Source} 创建，加载，压缩，
     * 响应写入事件。
     *
     * @param event 事件
     */
    @Override
    public void logOnEvent(Object event) {
        if (event instanceof DownloadContextEvent) {
            DownloadContextEvent dce = (DownloadContextEvent) event;
            DownloadContext context = dce.getContext();
            if (event instanceof DownloadStartedEvent ||
                    event instanceof AbstractCreateSourceEvent ||
                    event instanceof AbstractLoadSourceEvent ||
                    event instanceof AbstractCompressSourceEvent ||
                    event instanceof AbstractWriteResponseEvent ||
                    event instanceof SourceReleasedEvent ||
                    event instanceof SourceCacheDeletedEvent ||
                    event instanceof CompressionReleasedEvent ||
                    event instanceof CompressionCacheDeletedEvent) {
                log(context, dce.getMessage());
            }
        }
    }
}
