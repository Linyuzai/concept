package com.github.linyuzai.download.autoconfigure.logger;

import com.github.linyuzai.download.core.logger.DownloadLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonsDownloadLogger implements DownloadLogger {

    private final Log log = LogFactory.getLog(DownloadLogger.class);

    @Override
    public void info(String message) {
        log.info(TAG + message);
    }

    @Override
    public void error(String message, Throwable e) {
        log.error(TAG + message, e);
    }
}
