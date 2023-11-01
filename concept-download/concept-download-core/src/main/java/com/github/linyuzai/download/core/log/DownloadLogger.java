package com.github.linyuzai.download.core.log;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 下载日志抽象类。
 */
@CommonsLog
@NoArgsConstructor
@AllArgsConstructor
public abstract class DownloadLogger implements DownloadEventListener {

    /**
     * {@link Log} 缓存
     */
    private static final Map<String, Log> logs = new ConcurrentHashMap<>();

    /**
     * 标签
     */
    @Getter
    @Setter
    private String tag = "Download";

    /**
     * 用于标签长度对齐
     */
    @Getter
    @Setter
    private int tagLength = 8;

    @Getter
    @Setter
    private boolean enabled;

    @Override
    public void onEvent(Object event) {
        if (enabled) {
            logOnEvent(event);
        }
    }

    public abstract void logOnEvent(Object event);

    public Log getLog() {
        return log;
    }

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
        MethodParameter downloadMethod = context.getOptions().getMethodParameter();
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
