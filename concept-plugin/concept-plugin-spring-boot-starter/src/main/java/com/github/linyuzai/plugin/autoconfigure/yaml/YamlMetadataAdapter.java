package com.github.linyuzai.plugin.autoconfigure.yaml;

import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

/**
 * yaml插件元数据适配器
 */
public class YamlMetadataAdapter implements PluginMetadata.Adapter {

    @Override
    public boolean support(String name) {
        return PluginMetadata.YAML_NAME.equals(name) || PluginMetadata.YML_NAME.equals(name);
    }

    @Override
    public PluginMetadata adapt(InputStream is) {
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new InputStreamResource(is));
        return new PropertiesMetadata(factoryBean.getObject());
    }
}
