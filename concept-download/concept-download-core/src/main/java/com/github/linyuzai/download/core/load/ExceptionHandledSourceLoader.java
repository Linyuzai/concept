package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionHandledSourceLoader implements SourceLoader {

    private Source source;

    private SourceLoadExceptionHandler handler;

    @Override
    public boolean isAsyncLoad() {
        return source.isAsyncLoad();
    }

    @Override
    public SourceLoadResult load(DownloadContext context) {
        try {
            source.load(context);
            return new SourceLoadResult(source);
        } catch (Throwable e) {
            SourceLoadResult result = new SourceLoadResult(source, e);
            handler.onLoading(result.getException());
            return result;
        }
    }
}
