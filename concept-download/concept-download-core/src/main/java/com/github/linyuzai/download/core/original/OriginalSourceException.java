package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.exception.DownloadException;

/**
 * 下载源异常
 */
public class OriginalSourceException extends DownloadException {

    public OriginalSourceException(String message) {
        super(message);
    }

    public OriginalSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OriginalSourceException(Throwable cause) {
        super(cause);
    }
}
