package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.utils.DownloadUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 进度相关的事件的父类。
 */
@Getter
public abstract class ProgressEvent extends DownloadContextEvent {

    /**
     * 进度
     */
    private final Progress progress;

    public ProgressEvent(@NonNull DownloadContext context, Progress progress, String message) {
        super(context);
        this.progress = progress;
        setMessage(message);
    }

    /**
     * 将大小转为B，K，M等单位。
     *
     * @return 格式化后的数据
     */
    public String getBaseCurrentMessage() {
        return DownloadUtils.format(progress.getCurrent());
    }

    /**
     * 返回当前进度的格式化数据。
     *
     * @return 当前进度
     */
    public String getCurrentMessage() {
        return getBaseCurrentMessage();
    }

    /**
     * 如果存在总大小则返回比值，
     * 否则返回当前进度。
     *
     * @return 比值或当前进度
     */
    public String getRatioMessage() {
        Progress progress = getProgress();
        if (progress.hasTotal()) {
            return DownloadUtils.format(progress.getCurrent()) + "/" + DownloadUtils.format(progress.getTotal());
        } else {
            return getBaseCurrentMessage();
        }
    }

    /**
     * 如果存在总大小则返回百分比，
     * 否则返回当前进度。
     *
     * @return 百分比或当前进度
     */
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
