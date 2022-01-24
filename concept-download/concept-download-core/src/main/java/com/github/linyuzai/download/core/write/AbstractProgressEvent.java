package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.utils.DownloadUtils;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractProgressEvent extends DownloadContextEvent {

    private final Progress progress;

    public AbstractProgressEvent(@NonNull DownloadContext context, Progress progress, String message) {
        super(context);
        this.progress = progress;
        setMessage(message);
    }

    public String getBaseCurrentMessage() {
        return DownloadUtils.format(progress.getCurrent());
    }

    public String getCurrentMessage() {
        return getBaseCurrentMessage();
    }

    public String getRatioMessage() {
        Progress progress = getProgress();
        if (progress.hasTotal()) {
            return DownloadUtils.format(progress.getCurrent()) + "/" + DownloadUtils.format(progress.getTotal());
        } else {
            return getBaseCurrentMessage();
        }
    }

    public String getPercentageMessage() {
        Progress progress = getProgress();
        if (progress.hasTotal()) {
            double v = (progress.getCurrent() * 1.0 / progress.getTotal()) * 100.0;
            String format = String.format("%.2f", v);
            return format + "%";
        } else {
            return getBaseCurrentMessage();
        }
    }
}
