package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.write.Progress;
import com.github.linyuzai.download.core.write.ProgressDownloadEvent;

public class ResponseWritingProgressEvent extends ProgressDownloadEvent {

    public ResponseWritingProgressEvent(DownloadContext context, Progress progress) {
        super(context, progress, "Writing response " + progress.getCurrent() + "/" + progress.getTotal());
    }

    @Override
    public String getPercentageMessage() {
        return "Writing response " + calculatePercent();
    }
}
