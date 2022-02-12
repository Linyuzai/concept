package com.github.linyuzai.download.core.exception;

/**
 * 下载异常。
 */
public class DownloadException extends RuntimeException {

    public DownloadException(String message) {
        super(message);
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownloadException(Throwable cause) {
        super(cause);
    }
}
