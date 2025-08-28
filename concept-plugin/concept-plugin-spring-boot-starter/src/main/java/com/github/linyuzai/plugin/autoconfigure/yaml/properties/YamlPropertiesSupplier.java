package com.github.linyuzai.plugin.autoconfigure.yaml.properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.yaml.snakeyaml.Yaml;

import java.util.function.Supplier;

/**
 * 延迟加载 {@link Yaml}
 */
public interface YamlPropertiesSupplier extends Supplier<YamlPropertiesFactoryBean> {

}
