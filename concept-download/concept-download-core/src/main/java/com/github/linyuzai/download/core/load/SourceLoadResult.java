package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

@Getter
public class SourceLoadResult {

    private final Source source;

    private final SourceLoadException exception;

    public SourceLoadResult(Source source) {
        this.source = source;
        this.exception = null;
    }

    public SourceLoadResult(Source source, Throwable e) {
        this.source = source;
        this.exception = new SourceLoadException(source, e);
    }

    public boolean hasException() {
        return exception != null;
    }
}
