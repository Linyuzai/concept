package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.AbstractProgressEvent;
import com.github.linyuzai.download.core.write.Progress;

/**
 * {@link Source} 压缩进度更新时会发布该事件。
 */
public class SourceCompressingProgressEvent extends AbstractProgressEvent {

    private static final String CS = "Compressing source ";

    public SourceCompressingProgressEvent(DownloadContext context, Progress progress) {
        super(context, progress, CS + progress.getCurrent() + "/" + progress.getTotal());
    }

    /**
     * 返回当前进度的格式化数据。
     *
     * @return 当前进度
     */
    @Override
    public String getCurrentMessage() {
        return CS + super.getCurrentMessage();
    }

    /**
     * 如果存在总大小则返回比值，
     * 否则返回当前进度。
     *
     * @return 比值或当前进度
     */
    @Override
    public String getRatioMessage() {
        return CS + super.getRatioMessage();
    }

    /**
     * 如果存在总大小则返回百分比，
     * 否则返回当前进度。
     *
     * @return 百分比或当前进度
     */
    @Override
    public String getPercentageMessage() {
        return CS + super.getPercentageMessage();
    }
}
