package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.AbstractProgressEvent;
import com.github.linyuzai.download.core.write.Progress;
import lombok.Getter;

/**
 * {@link Source} 加载进度更新时会发布该事件。
 */
@Getter
public class SourceLoadingProgressEvent extends AbstractProgressEvent {

    private static final String L = "Loading ";

    private final Source source;

    public SourceLoadingProgressEvent(DownloadContext context, Source source, Progress progress) {
        super(context, progress, getLS(source) + progress.getCurrent() + "/" + progress.getTotal());
        this.source = source;
    }

    @Override
    public String getCurrentMessage() {
        return getLS() + super.getCurrentMessage();
    }

    @Override
    public String getRatioMessage() {
        return getLS() + super.getRatioMessage();
    }

    @Override
    public String getPercentageMessage() {
        return getLS() + super.getPercentageMessage();
    }

    public String getLS() {
        return getLS(source);
    }

    public static String getLS(Source source) {
        return L + source.getDescription() + " ";
    }
}
