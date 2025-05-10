package com.github.linyuzai.plugin.core.metadata;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public abstract class AbstractPluginMetadataFactory implements PluginMetadataFactory {

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
