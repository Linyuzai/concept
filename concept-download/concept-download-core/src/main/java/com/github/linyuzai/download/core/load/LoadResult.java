package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoadResult {

    private Source source;

    private Throwable throwable;

    public LoadResult(Source source) {
        this.source = source;
    }

    public boolean hasException() {
        return throwable != null;
    }
}
