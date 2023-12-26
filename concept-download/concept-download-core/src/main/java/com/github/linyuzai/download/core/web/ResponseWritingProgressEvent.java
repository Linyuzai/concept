package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.write.Progress;
import com.github.linyuzai.download.core.write.ProgressEvent;

/**
 * {@link DownloadResponse} 写入进度更新时会发布该事件。
 */
public class ResponseWritingProgressEvent extends ProgressEvent {

    public ResponseWritingProgressEvent(DownloadContext context, Progress progress) {
        super(context, progress);
    }
}
