package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.write.AbstractProgressEvent;
import com.github.linyuzai.download.core.write.Progress;

public class SourceCompressingProgressEvent extends AbstractProgressEvent {

    private static final String CS = "Compressing source ";

    public SourceCompressingProgressEvent(DownloadContext context, Progress progress) {
        super(context, progress, CS + progress.getCurrent() + "/" + progress.getTotal());
    }

    @Override
    public String getCurrentMessage() {
        return CS + super.getCurrentMessage();
    }

    @Override
    public String getRatioMessage() {
        return CS + super.getRatioMessage();
    }

    @Override
    public String getPercentageMessage() {
        return CS + super.getPercentageMessage();
    }
}
