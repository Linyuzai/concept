/*
package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.Charset;

*/
/**
 * 支持 {@link Publisher} 的 {@link Source}。
 *//*

@Getter
@AllArgsConstructor
public class PublisherSource implements Source {

    private Publisher<?> publisher;

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Charset getCharset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getLength() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncLoad() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSingle() {
        throw new UnsupportedOperationException();
    }

    */
/**
     * 使用 {@link Flux#from(Publisher)} 来获得 {@link Source}。
     *
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     *//*

    @Override
    public void load(DownloadContext context) {
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        return Flux.from(publisher)
                .collectList()
                .map(it -> adapter.getFactory(it, context).create(it, context))
                .flatMap(it -> it.load(context));
    }
}
*/
