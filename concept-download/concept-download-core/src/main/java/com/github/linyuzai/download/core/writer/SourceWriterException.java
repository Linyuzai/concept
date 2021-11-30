package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.exception.DownloadException;

/**
 * 下载源异常
 */
public class SourceWriterException extends DownloadException {

    public SourceWriterException(String message) {
        super(message);
    }

    public SourceWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceWriterException(Throwable cause) {
        super(cause);
    }
}
