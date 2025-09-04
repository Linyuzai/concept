package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public abstract class AbstractPluginFactory implements PluginFactory, PluginMetadataFactory {

    private Collection<PluginMetadata.Adapter> metadataAdapters = new ArrayList<>();

    protected PluginMetadata.Adapter getMetadataAdapter(String name) {
        for (PluginMetadata.Adapter adapter : metadataAdapters) {
            if (adapter.support(name)) {
                return adapter;
            }
        }
        return null;
    }

}
