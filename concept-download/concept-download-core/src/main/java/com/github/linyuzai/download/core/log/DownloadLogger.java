package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.options.DownloadMethod;
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
public abstract class DownloadLogger implements DownloadEventListener {

    private static final Map<String, Log> logs = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private String tag = "Download";

    @Getter
    @Setter
    private int tagLength = 8;

    public Log getLog() {
        return log;
    }

    public void log(DownloadContext context, String message) {
        log(context, tag, message);
    }

    public void log(DownloadContext context, String tag, String message) {
        DownloadMethod downloadMethod = context.getOptions().getDownloadMethod();
        if (downloadMethod == null) {
            log(getLog(), tag, message);
        } else {
            Method method = downloadMethod.getMethod();
            log(method.getDeclaringClass().getSimpleName() + "#" + method.getName(), tag, message);
        }
    }

    public void log(String log, String message) {
        log(log, tag, message);
    }

    public void log(String log, String tag, String message) {
        log(logs.computeIfAbsent(log, LogFactory::getLog), tag, message);
    }

    public void log(Log log, String message) {
        log(log, tag, message);
    }

    public void log(Log log, String tag, String message) {
        log.info(appendTag(tag) + message + " ");
    }

    public String appendTag(String tag) {
        StringBuilder builder = new StringBuilder(tag);
        while (builder.length() < tagLength) {
            builder.append(" ");
        }
        return builder.append(" >> ").toString();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 100;
    }
}
