package com.github.linyuzai.download.core.logger;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.event.DownloadStartedEvent;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 下载日志抽象类。
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class LoggingDownloadEventListener implements DownloadEventListener {

    public static final String LOG_ID = "_download@log_id";

    private boolean enabled;

    @Override
    public void onEvent(Object event) {
        if (enabled) {
            if (event instanceof DownloadStartedEvent) {
                DownloadContext context = ((DownloadStartedEvent) event).getContext();
                if (!hasLogId(context)) {
                    setLogId(context, generateLogId());
                }
            }
            logOnEvent(event);
        }
    }

    public abstract void logOnEvent(Object event);

    /**
     * 使用类名加方法名作为日志名称。
     *
     * @param context {@link DownloadContext}
     * @param message 信息
     */
    public void log(DownloadContext context, String message) {
        DownloadOptions options = DownloadOptions.get(context);
        Method method = options.getMethod();
        DownloadLogger logger = context.get(DownloadLogger.class);
        logger.info(getTag(method) + message);
    }

    protected String getTag(Method method) {
        if (method == null) {
            return "";
        } else {
            return method.getDeclaringClass().getSimpleName() + "#" + method.getName() + " ";
        }
    }

    protected String generateLogId() {
        return UUID.randomUUID().toString();
    }

    protected boolean hasLogId(DownloadContext context) {
        return context.contains(LOG_ID);
    }

    protected void setLogId(DownloadContext context, String logId) {
        context.set(LOG_ID, logId);
    }

    protected String getLogId(DownloadContext context) {
        return context.get(LOG_ID);
    }
}
