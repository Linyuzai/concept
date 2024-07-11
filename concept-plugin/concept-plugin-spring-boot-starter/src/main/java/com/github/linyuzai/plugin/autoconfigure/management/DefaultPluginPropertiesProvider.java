package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class DefaultPluginPropertiesProvider implements PluginPropertiesProvider {

    protected final PluginLocation location;

    protected final PluginConcept concept;

    protected final List<PluginFactory> factories;

    @Override
    public List<PluginPropertyEntry> getProperties(String group, String name) {
        return collect(getMetadata(group, name));
    }

    protected PluginMetadata getMetadata(String group, String name) {
        String path = location.getLoadedPluginPath(group, name);
        Plugin plugin = concept.getRepository().get(path);
        if (plugin == null) {
            PluginMetadata metadata = getMetadata(location.getUnloadedPluginPath(group, name));
            if (metadata == null) {
                return getMetadata(path);
            } else {
                return metadata;
            }
        } else {
            return plugin.getMetadata();
        }
    }

    public PluginMetadata getMetadata(String path) {
        for (PluginFactory factory : factories) {
            PluginMetadataFactory metadataFactory = factory.getMetadataFactory();
            if (metadataFactory == null) {
                continue;
            }
            try {
                PluginMetadata metadata = metadataFactory.create(path);
                if (metadata == null) {
                    continue;
                }
                return metadata;
            } catch (Throwable ignore) {
            }
        }
        return null;
    }

    public static List<PluginPropertyEntry> collect(PluginMetadata metadata) {
        List<PluginPropertyEntry> properties = new ArrayList<>();
        if (metadata == null) {
            return properties;
        }
        Set<String> names = metadata.names();
        for (String n : names) {
            String v = metadata.get(n);
            PluginPropertyEntry property = new PluginPropertyEntry();
            property.setName(n);
            property.setValue(v);
            properties.add(property);
        }
        return properties;
    }
}
