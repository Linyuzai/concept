package com.github.linyuzai.plugin.core.metadata;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

@Getter
@Setter
public abstract class AbstractPluginMetadataFinder implements PluginMetadataFinder {

    private Collection<Adapter> adapters = Collections.emptyList();

    protected Adapter getAdapter(String name) {
        for (Adapter adapter : adapters) {
            if (adapter.support(name)) {
                return adapter;
            }
        }
        return null;
    }

    public interface Adapter {

        boolean support(String name);

        PluginMetadata adapt(InputStream is) throws IOException;
    }
}
