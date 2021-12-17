package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * 加载结果 / Result of loading
 */
@Getter
public class SourceLoadResult {

    private final Source source;

    private final SourceLoadException exception;

    /**
     * 成功的结果 / Successful result
     *
     * @param source 被加载的下载源 / Loaded download source
     */
    public SourceLoadResult(Source source) {
        this.source = source;
        this.exception = null;
    }

    /**
     * 异常的结果 / Exception result
     *
     * @param source 被加载的下载源 / Loaded download source
     * @param e      异常 / exception
     */
    public SourceLoadResult(Source source, Throwable e) {
        this.source = source;
        this.exception = new SourceLoadException(source, e);
    }

    /**
     * @return 是否有异常 / If any exception
     */
    public boolean hasException() {
        return exception != null;
    }
}
