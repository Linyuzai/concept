package com.github.linyuzai.download.core.logger;

import com.github.linyuzai.download.core.compress.*;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadCompletedEvent;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.event.DownloadStartedEvent;
import com.github.linyuzai.download.core.load.SourceAlreadyLoadedEvent;
import com.github.linyuzai.download.core.load.SourceLoadedEvent;
import com.github.linyuzai.download.core.load.SourceLoadedUsingCacheEvent;
import com.github.linyuzai.download.core.source.*;
import com.github.linyuzai.download.core.utils.DownloadUtils;
import com.github.linyuzai.download.core.web.ResponseWrittenEvent;

import java.io.File;

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
            if (event instanceof DownloadStartedEvent) {
                log(context, "Context initialized");
            } else if (event instanceof DownloadCompletedEvent) {
                log(context, "Context destroyed");
            } else if (event instanceof SourceCreatedEvent) {
                log(context, "Source created: " + getSource(event));
            } else if (event instanceof SourceLoadedEvent) {
                log(context, "Source loaded: " + getSource(event));
            } else if (event instanceof SourceAlreadyLoadedEvent) {
                log(context, "Source load skip for already loaded: " + getSource(event));
            } else if (event instanceof SourceLoadedUsingCacheEvent) {
                String cache = ((SourceLoadedUsingCacheEvent) event).getCache();
                log(context, "Source load using cache " + cache + ": " + getSource(event));
            } else if (event instanceof SourceCompressedEvent) {
                Source source = ((SourceCompressedEvent) event).getSource();
                Compression compression = ((SourceCompressedEvent) event).getCompression();
                log(context, "Source compressed " + DownloadUtils.formatCompressedSize(source, compression));
            } else if (event instanceof SourceNoCompressionEvent) {
                log(context, "Source compress skip: " + getSource(event));
            } else if (event instanceof SourceCompressedUsingCacheEvent) {
                String cache = ((SourceCompressedUsingCacheEvent) event).getCache();
                log(context, "Source compress using cache " + cache + ": " + getSource(event));
            } else if (event instanceof SourceCompressionFormatEvent) {
                String format = ((SourceCompressionFormatEvent) event).getFormat();
                log(context, "Source compress using " + format + ": " + getSource(event));
            } else if (event instanceof SourceFileCompressionEvent) {
                File file = ((SourceFileCompressionEvent) event).getFile();
                log(context, "Source compress with file " + file.getAbsolutePath() + ": " + getSource(event));
            } else if (event instanceof SourceMemoryCompressionEvent) {
                log(context, "Source compress in memory: " + getSource(event));
            } else if (event instanceof ResponseWrittenEvent) {
                log(context, "Response written");
            } else if (event instanceof SourceCacheDeletedEvent) {
                log(context, "Source cache deleted: " + getSource(event));
            } else if (event instanceof SourceReleasedEvent) {
                log(context, "Source resource released: " + getSource(event));
            } else if (event instanceof CompressionCacheDeletedEvent) {
                log(context, "Compression cache deleted: " + getCompression(event));
            } else if (event instanceof CompressionReleasedEvent) {
                log(context, "Compression resource released: " + getCompression(event));
            }
        }
    }

    protected String getSource(Object event) {
        if (event instanceof AbstractSourceEvent) {
            return ((AbstractSourceEvent) event).getSource().getDescription();
        }
        return "[Unknown]";
    }

    protected String getCompression(Object event) {
        if (event instanceof AbstractCompressionEvent) {
            return ((AbstractCompressionEvent) event).getCompression().getDescription();
        }
        return "[Unknown]";
    }
}
