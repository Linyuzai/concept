package com.github.linyuzai.plugin.autoconfigure.yaml.properties;

import com.github.linyuzai.plugin.core.handle.extract.convert.AbstractPluginConvertor;
import org.yaml.snakeyaml.Yaml;

import java.util.Properties;


/**
 * {@link Yaml} 转换器
 */
public class YamlToPropertiesConvertor extends AbstractPluginConvertor<YamlPropertiesSupplier, Properties> {

    @Override
    public Properties doConvert(YamlPropertiesSupplier source) {
        return source.get().getObject();
    }
}
