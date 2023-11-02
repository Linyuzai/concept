package com.github.linyuzai.download.core.log;

public interface DownloadLogger {

    void info(String message);

    void error(String message, Throwable e);
}
