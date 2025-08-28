package com.github.linyuzai.plugin.autoconfigure.yaml.properties;

import com.github.linyuzai.plugin.core.metadata.AbstractPluginMetadataFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

public class YamlPropertiesMetadataAdapter implements AbstractPluginMetadataFactory.Adapter {

    @Override
    public boolean support(String name) {
        return PluginMetadata.YAML_NAME.equals(name) || PluginMetadata.YML_NAME.equals(name);
    }

    @Override
    public PluginMetadata adapt(InputStream is) throws IOException {
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new InputStreamResource(is));
        return new PropertiesMetadata(factoryBean.getObject());
    }
}
