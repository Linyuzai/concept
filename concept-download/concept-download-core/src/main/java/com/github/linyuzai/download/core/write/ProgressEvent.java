package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;
import lombok.NonNull;

/**
 * 进度相关的事件的父类。
 */
@Getter
public abstract class ProgressEvent extends DownloadContextEvent {

    /**
     * 进度
     */
    private final Progress progress;

    public ProgressEvent(@NonNull DownloadContext context, Progress progress) {
        super(context);
        this.progress = progress;
    }
}
