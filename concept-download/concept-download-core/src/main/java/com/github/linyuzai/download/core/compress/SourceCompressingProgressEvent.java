package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.Progress;
import com.github.linyuzai.download.core.write.ProgressEvent;

/**
 * {@link Source} 压缩进度更新时会发布该事件。
 */
public class SourceCompressingProgressEvent extends ProgressEvent {

    public SourceCompressingProgressEvent(DownloadContext context, Progress progress) {
        super(context, progress);
    }
}
