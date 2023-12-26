package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.Getter;

@Getter
public class DownloadErrorEvent extends DownloadContextEvent {

    private final Throwable error;

    public DownloadErrorEvent(DownloadContext context, Throwable error) {
        super(context);
        this.error = error;
    }
}
