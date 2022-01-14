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
    public void log(String message) {
        DownloadMethod downloadMethod = options.getDownloadMethod();
        if (downloadMethod == null) {
            log(log, message);
        } else {
            Method method = downloadMethod.getMethod();
            log(method.getDeclaringClass().getSimpleName() + "." + method.getName(), message);
        }
    }

    @Override
    public void log(String log, String message) {
        log(logs.computeIfAbsent(log, LogFactory::getLog), message);
    }

    @Override
    public void log(Log log, String message) {
        if (getOptions().isLogEnabled()) {
            log.info(message);
        }
    }
}
