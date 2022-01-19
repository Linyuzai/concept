package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class ProgressDownloadEvent extends DownloadContextEvent {

    private final Progress progress;

    public ProgressDownloadEvent(@NonNull DownloadContext context, Progress progress, String message) {
        super(context);
        this.progress = progress;
        setMessage(message);
    }

    public String calculatePercent() {
        Progress progress = getProgress();
        double v = (progress.getCurrent() * 1.0 / progress.getTotal()) * 100.0;
        String format = String.format("%.2f", v);
        return format + "%";
    }

    public abstract String getPercentageMessage();
}
