package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
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
        return format(progress.getCurrent());
    }

    public String getCurrentMessage() {
        return getBaseCurrentMessage();
    }

    public String getRatioMessage() {
        Progress progress = getProgress();
        if (progress.hasTotal()) {
            return format(progress.getCurrent()) + "/" + format(progress.getTotal());
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

    public String format(long size) {
        if (size >= 1024) {
            double k = size / 1024.0;
            if (k >= 1024) {
                double m = k / 1024;
                return String.format("%.2f", m) + "M";
            } else {
                return String.format("%.2f", k) + "K";
            }
        } else {
            return size + "B";
        }
    }
}
