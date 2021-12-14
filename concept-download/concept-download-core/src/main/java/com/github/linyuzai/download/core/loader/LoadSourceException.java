package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

public class LoadSourceException extends DownloadException {

    @Getter
    private final Source source;

    public LoadSourceException(Source source, Throwable cause) {
        super(cause);
        this.source = source;
    }
}
