package com.github.linyuzai.download.core.logger;

public interface DownloadLogger {

    String TAG = "Download >> ";

    void info(String message);

    void error(String message, Throwable e);
}
