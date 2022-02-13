package com.github.linyuzai.download.core.web;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.write.AbstractProgressEvent;
import com.github.linyuzai.download.core.write.Progress;

/**
 * {@link DownloadResponse} 写入进度更新时会发布该事件。
 */
public class ResponseWritingProgressEvent extends AbstractProgressEvent {

    private static final String WR = "Writing response ";

    public ResponseWritingProgressEvent(DownloadContext context, Progress progress) {
        super(context, progress, WR + progress.getCurrent() + "/" + progress.getTotal());
    }

    /**
     * 返回当前进度的格式化数据。
     *
     * @return 当前进度
     */
    @Override
    public String getCurrentMessage() {
        return WR + super.getCurrentMessage();
    }

    /**
     * 如果存在总大小则返回比值，
     * 否则返回当前进度。
     *
     * @return 比值或当前进度
     */
    @Override
    public String getRatioMessage() {
        return WR + super.getRatioMessage();
    }

    /**
     * 如果存在总大小则返回百分比，
     * 否则返回当前进度。
     *
     * @return 百分比或当前进度
     */
    @Override
    public String getPercentageMessage() {
        return WR + super.getPercentageMessage();
    }
}
