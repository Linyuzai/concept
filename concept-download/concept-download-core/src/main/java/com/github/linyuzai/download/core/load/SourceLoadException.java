package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

public class SourceLoadException extends DownloadException {

    @Getter
    private final Source source;

    public SourceLoadException(Source source, Throwable cause) {
        super(cause);
        this.source = source;
    }
}
