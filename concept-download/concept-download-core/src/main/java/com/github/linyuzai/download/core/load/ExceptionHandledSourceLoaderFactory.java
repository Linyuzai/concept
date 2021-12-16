package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionHandledSourceLoaderFactory implements SourceLoaderFactory {

    private SourceLoadExceptionHandler handler;

    @Override
    public SourceLoader create(Source source, DownloadContext context) {
        return new ExceptionHandledSourceLoader(source, handler);
    }
}
