package com.github.linyuzai.download.core.range;

import com.github.linyuzai.download.core.exception.DownloadException;

public class RangeReadException extends DownloadException {

    public RangeReadException(String message) {
        super(message);
    }

    public RangeReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangeReadException(Throwable cause) {
        super(cause);
    }
}
