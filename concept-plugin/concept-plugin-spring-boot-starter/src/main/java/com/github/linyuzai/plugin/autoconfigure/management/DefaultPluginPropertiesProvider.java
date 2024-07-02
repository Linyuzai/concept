package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.factory.ZipPluginFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class DefaultPluginPropertiesProvider implements PluginPropertiesProvider {

    protected final PluginLocation location;

    protected final PluginConcept concept;

    @Override
    public List<PluginPropertyEntry> getProperties(String group, String name) {
        return collect(getMetadata(group, name));
    }

    protected PluginMetadata getMetadata(String group, String name) {
        String path = location.getLoadedPluginPath(group, name);
        Plugin plugin = concept.getRepository().get(path);
        if (plugin == null) {
            try {
                File file = new File(location.getUnloadedPluginPath(group, name));
                return ZipPluginFactory.createMetadata(file);
            } catch (IOException e) {
                try {
                    return ZipPluginFactory.createMetadata(new File(path));
                } catch (IOException ex) {
                    return null;
                }
            }
        } else {
            return plugin.getMetadata();
        }
    }

    public static List<PluginPropertyEntry> collect(PluginMetadata metadata) {
        List<PluginPropertyEntry> properties = new ArrayList<>();
        if (metadata == null) {
            return properties;
        }
        Set<String> names = metadata.names();
        for (String n : names) {
            String v = metadata.get(n);
            PluginPropertyEntry property = new PluginPropertyEntry<>();
            property.setName(n);
            property.setValue(v);
            properties.add(property);
        }
        return properties;
    }
}
