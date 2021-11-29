package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.exception.DownloadException;

public class OriginalSourceCompressException extends DownloadException {

    public OriginalSourceCompressException(String message) {
        super(message);
    }

    public OriginalSourceCompressException(String message, Throwable cause) {
        super(message, cause);
    }

    public OriginalSourceCompressException(Throwable cause) {
        super(cause);
    }
}
