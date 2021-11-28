package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.exception.DownloadException;

/**
 * 下载源异常
 */
public class DownloadSourceException extends DownloadException {

    public DownloadSourceException(String message) {
        super(message);
    }

    public DownloadSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownloadSourceException(Throwable cause) {
        super(cause);
    }
}
