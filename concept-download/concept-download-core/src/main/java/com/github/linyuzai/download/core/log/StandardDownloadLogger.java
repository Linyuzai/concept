package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadContextEvent;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.load.SourceLoadedEvent;
import com.github.linyuzai.download.core.options.DownloadMethod;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CommonsLog
@NoArgsConstructor
@AllArgsConstructor
public class StandardDownloadLogger implements DownloadEventListener {

    private static final Map<String, Log> logs = new ConcurrentHashMap<>();

    private static String TAG = "Download";

    @Getter
    @Setter
    private int tagLength = 8;

    @Override
    public void onEvent(Object event) {
        if (event instanceof DownloadContextEvent) {
            DownloadContextEvent dce = (DownloadContextEvent) event;
            DownloadContext context = dce.getContext();
            if (event instanceof SourceCreatedEvent) {
                if (event.getClass() == SourceCreatedEvent.class) {
                    log(context, TAG, "All sources created");
                } else {
                    log(context, TAG, dce.getMessage());
                }
            } else if (event instanceof SourceLoadedEvent) {
                if (event.getClass() == SourceLoadedEvent.class) {
                    log(context, TAG, "All sources loaded");
                } else {
                    log(context, TAG, dce.getMessage());
                }
            } else {
                log(context, TAG, dce.getMessage());
            }
        }
    }

    public void log(DownloadContext context, String tag, String message) {
        DownloadMethod downloadMethod = context.getOptions().getDownloadMethod();
        if (downloadMethod == null) {
            log(log, tag, message);
        } else {
            Method method = downloadMethod.getMethod();
            log(method.getDeclaringClass().getSimpleName() + "#" + method.getName(), tag, message);
        }
    }

    public void log(String log, String tag, String message) {
        log(logs.computeIfAbsent(log, LogFactory::getLog), tag, message);
    }

    public void log(Log log, String tag, String message) {
        log.info(appendTag(tag) + message);
    }

    private String appendTag(String tag) {
        StringBuilder builder = new StringBuilder(tag);
        while (builder.length() < tagLength) {
            builder.append(" ");
        }
        return builder.append(" >> ").toString();
    }
}
