package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.write.AbstractProgressEvent;
import com.github.linyuzai.download.core.write.Progress;

public class ResponseWritingProgressEvent extends AbstractProgressEvent {

    private static final String WR = "Writing response ";

    public ResponseWritingProgressEvent(DownloadContext context, Progress progress) {
        super(context, progress, WR + progress.getCurrent() + "/" + progress.getTotal());
    }

    @Override
    public String getCurrentMessage() {
        return WR + super.getCurrentMessage();
    }

    @Override
    public String getRatioMessage() {
        return WR + super.getRatioMessage();
    }

    @Override
    public String getPercentageMessage() {
        return WR + super.getPercentageMessage();
    }
}
