package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.Progress;
import com.github.linyuzai.download.core.write.ProgressDownloadEvent;
import lombok.Getter;

@Getter
public class SourceLoadingProgressEvent extends ProgressDownloadEvent {

    private final Source source;

    public SourceLoadingProgressEvent(DownloadContext context, Source source, Progress progress) {
        super(context, progress, "Loading " + source + " " + progress.getCurrent() + "/" + progress.getTotal());
        this.source = source;
    }

    @Override
    public String getPercentageMessage() {
        return "Loading " + source.getDescription() + " " + calculatePercent();
    }
}
