package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 加载处理器 / A handler to process loads
 */
@AllArgsConstructor
public class LoadSourceHandler implements DownloadHandler {

    private SourceLoaderFactory sourceLoaderFactory;

    private SourceLoaderInvoker sourceLoaderInvoker;

    private SourceLoadExceptionHandler sourceLoadExceptionHandler;

    /**
     * 将所有的Source封装成对应的加载器 / Encapsulate all sources into corresponding loaders
     * 使用Invoker调用加载器 / Invoking the loader using invoker
     * 处理加载异常 / Handle load exception
     *
     * @param context 下载上下文 / Context of download
     * @param chain   处理链 / Chain of handler
     * @throws IOException I/O exception
     */
    @Override
    public void handle(DownloadContext context, DownloadHandlerChain chain) throws IOException {
        Source source = context.get(Source.class);
        Collection<SourceLoader> loaders = new ArrayList<>();
        Collection<Source> sources = source.list();
        for (Source s : sources) {
            SourceLoader loader = sourceLoaderFactory.create(s, context);
            loaders.add(loader);
        }
        Collection<SourceLoadResult> results = sourceLoaderInvoker.invoke(loaders, context);
        List<SourceLoadException> exceptions = results.stream()
                .filter(SourceLoadResult::hasException)
                .map(SourceLoadResult::getException)
                .collect(Collectors.toList());
        if (!exceptions.isEmpty()) {
            sourceLoadExceptionHandler.onLoaded(exceptions);
        }
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_LOAD_SOURCE;
    }
}
