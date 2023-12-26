package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.Progress;
import com.github.linyuzai.download.core.write.ProgressEvent;
import lombok.Getter;

/**
 * {@link Source} 加载进度更新时会发布该事件。
 */
@Getter
public class SourceLoadingProgressEvent extends ProgressEvent {

    private final Source source;

    public SourceLoadingProgressEvent(DownloadContext context, Source source, Progress progress) {
        super(context, progress);
        this.source = source;
    }
}
