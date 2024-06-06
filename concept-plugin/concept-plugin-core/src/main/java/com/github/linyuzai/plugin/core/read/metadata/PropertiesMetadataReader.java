package com.github.linyuzai.plugin.core.read.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.content.PluginContent;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class PropertiesMetadataReader implements MetadataReader {

    private final Properties properties = new Properties();

    public PropertiesMetadataReader(Plugin plugin) throws IOException {
        this(plugin, "plugin.properties");
    }

    public PropertiesMetadataReader(Plugin plugin, String filename) throws IOException {
        PluginContent metadata = plugin.read(PluginContent.class, filename);
        try (InputStream is = metadata.getInputStream()) {
            if (is != null) {
                properties.load(is);
            }
        }
    }

    @Override
    public PluginMetadata read(Object key, PluginContext context) {
        return new PropertiesMetadata();
    }

    public class PropertiesMetadata implements PluginMetadata {

        @Override
        public String get(String key) {
            return properties.getProperty(key);
        }
    }
}
