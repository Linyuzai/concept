package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.order.OrderProvider;

import java.util.Comparator;
import java.util.List;

public class DefaultSourceFactoryAdapter implements SourceFactoryAdapter, DownloadContextInitializer {

    private final List<SourceFactory> factories;

    public DefaultSourceFactoryAdapter(List<SourceFactory> factories) {
        this.factories = factories;
        this.factories.sort(Comparator.comparingInt(OrderProvider::getOrder));
    }

    @Override
    public SourceFactory getFactory(Object source, DownloadContext context) {
        for (SourceFactory factory : factories) {
            if (factory.support(source, context)) {
                return factory;
            }
        }
        throw new DownloadException("No SourceFactory support: " + source);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceFactoryAdapter.class, this);
    }
}
