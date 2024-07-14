package com.github.linyuzai.plugin.autoconfigure.logger;

import com.github.linyuzai.plugin.core.logger.PluginLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 基于 common log 的插件日志
 */
public class CommonsPluginLogger implements PluginLogger {

    private final Log log = LogFactory.getLog(PluginLogger.class);

    @Override
    public void info(String message) {
        log.info(TAG + message);
    }

    @Override
    public void error(String message, Throwable e) {
        log.error(TAG + message, e);
    }
}
