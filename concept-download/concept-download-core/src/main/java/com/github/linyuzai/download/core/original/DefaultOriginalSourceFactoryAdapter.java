package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.order.OrderProvider;

import java.util.Comparator;
import java.util.List;

public class DefaultOriginalSourceFactoryAdapter implements OriginalSourceFactoryAdapter, DownloadContextInitializer {

    private final List<OriginalSourceFactory> factories;

    public DefaultOriginalSourceFactoryAdapter(List<OriginalSourceFactory> factories) {
        this.factories = factories;
        this.factories.sort(Comparator.comparingInt(OrderProvider::getOrder));
    }

    @Override
    public OriginalSourceFactory getOriginalSourceFactory(Object source, DownloadContext context) {
        for (OriginalSourceFactory factory : factories) {
            if (factory.support(source, context)) {
                return factory;
            }
        }
        throw new OriginalSourceException("No OriginalSourceFactory support: " + source);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(OriginalSourceFactoryAdapter.class, this);
    }
}
