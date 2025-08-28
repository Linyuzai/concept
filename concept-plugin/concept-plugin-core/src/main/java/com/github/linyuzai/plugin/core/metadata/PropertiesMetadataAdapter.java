package com.github.linyuzai.plugin.core.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesMetadataAdapter implements AbstractPluginMetadataFactory.Adapter {

    @Override
    public boolean support(String name) {
        return PluginMetadata.PROP_NAME.equals(name);
    }

    @Override
    public PluginMetadata adapt(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        return new PropertiesMetadata(properties);
    }
}
