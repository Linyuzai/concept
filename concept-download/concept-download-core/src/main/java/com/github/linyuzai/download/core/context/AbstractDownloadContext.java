package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadMethod;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持有下载操作参数的下载上下文 / Context of download holding download options
 */
@CommonsLog
@Getter
@AllArgsConstructor
public abstract class AbstractDownloadContext implements DownloadContext {

    private static final Map<String, Log> logs = new ConcurrentHashMap<>();

    @NonNull
    private final DownloadOptions options;

    @Override
    public void log(String tag, String message) {
        DownloadMethod downloadMethod = getOptions().getDownloadMethod();
        if (downloadMethod == null) {
            log(log, tag, message);
        } else {
            Method method = downloadMethod.getMethod();
            log(method.getDeclaringClass().getSimpleName() + "#" + method.getName(), tag, message);
        }
    }

    @Override
    public void log(String log, String tag, String message) {
        log(logs.computeIfAbsent(log, LogFactory::getLog), tag, message);
    }

    @Override
    public void log(Log log, String tag, String message) {
        if (getOptions().isLogEnabled()) {
            log.info(appendTag(tag, getOptions().getLogTagLength()) + message);
        }
    }

    private String appendTag(String tag, int length) {
        StringBuilder builder = new StringBuilder(tag);
        while (builder.length() < length) {
            builder.append(" ");
        }
        return builder.append(" >> ").toString();
    }
}
