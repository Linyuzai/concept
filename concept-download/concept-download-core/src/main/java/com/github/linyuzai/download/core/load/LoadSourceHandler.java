package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 加载处理器 / A handler to process loads
 */
@AllArgsConstructor
public class LoadSourceHandler implements DownloadHandler {

    //private SourceLoaderFactory sourceLoaderFactory;

    //private SourceLoaderInvoker sourceLoaderInvoker;

    //private SourceLoadExceptionHandler sourceLoadExceptionHandler;

    private SourceLoader sourceLoader;

    /**
     * 将所有的Source封装成对应的加载器 / Encapsulate all sources into corresponding loaders
     * 使用Invoker调用加载器 / Invoking the loader using invoker
     * 处理加载异常 / Handle load exception
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        Source source = context.get(Source.class);
        return sourceLoader.load(source, context)
                .flatMap(it -> chain.next(context));
        /*return source.flatMap(it -> {
            Collection<SourceLoader> loaders = new ArrayList<>();
            Collection<Source> sources = it.list();
            for (Source s : sources) {
                SourceLoader loader = sourceLoaderFactory.create(s, context);
                loaders.add(loader);
            }


            return sourceLoaderInvoker.invoke(loaders, context).flatMap(rs -> {
                List<? extends Mono<? extends SourceLoadResult>> monos = rs.stream()
                        .map(Mono::just)
                        .collect(Collectors.toList());
                return Mono.zip(monos, objects -> Arrays.stream(objects)
                        .map(SourceLoadResult.class::cast)
                        .collect(Collectors.toList()));
            }).map(results -> {
                List<SourceLoadException> exceptions = results.stream()
                        .filter(SourceLoadResult::hasException)
                        .map(SourceLoadResult::getException)
                        .collect(Collectors.toList());
                if (!exceptions.isEmpty()) {
                    sourceLoadExceptionHandler.onLoaded(exceptions);
                }
                return it;
            });
        }).flatMap(it -> chain.next(context));*/
    }

    @Override
    public int getOrder() {
        return ORDER_LOAD_SOURCE;
    }
}
