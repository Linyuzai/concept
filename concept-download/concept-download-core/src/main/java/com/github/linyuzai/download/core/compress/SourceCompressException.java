package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.exception.DownloadException;

public class SourceCompressException extends DownloadException {

    public SourceCompressException(String message) {
        super(message);
    }

    public SourceCompressException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceCompressException(Throwable cause) {
        super(cause);
    }
}
