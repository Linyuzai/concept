package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * 下载日志抽象类。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class LoggingDownloadEventListener implements DownloadEventListener {

    public static final String LOG_ID = "_download_log_id";

    /**
     * 标签
     */
    private String tag = "Download";

    /**
     * 用于标签长度对齐
     */
    private int tagLength = 8;

    private boolean enabled;

    @Override
    public void onEvent(Object event) {
        if (enabled) {
            logOnEvent(event);
        }
    }

    public abstract void logOnEvent(Object event);

    public void log(DownloadContext context, String message) {
        log(context, tag, message);
    }

    /**
     * 使用类名加方法名作为日志名称。
     *
     * @param context {@link DownloadContext}
     * @param tag     标签
     * @param message 信息
     */
    public void log(DownloadContext context, String tag, String message) {
        String info;
        DownloadOptions options = context.get(DownloadOptions.class);
        Method method = options.getMethod();
        if (method == null) {
            info = tag + message;
        } else {
            info = method.getDeclaringClass().getSimpleName() + "#" + method.getName() + tag + message;
        }
        DownloadLogger logger = context.get(DownloadLogger.class);
        logger.info(info);
    }

    public String appendTag(String tag) {
        StringBuilder builder = new StringBuilder(tag);
        while (builder.length() < tagLength) {
            builder.append(" ");
        }
        return builder.append(" >> ").toString();
    }
}
