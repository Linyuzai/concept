package com.github.linyuzai.plugin.autoconfigure.yaml;

import org.yaml.snakeyaml.Yaml;

import java.util.function.Supplier;

/**
 * 延迟加载 {@link Yaml}
 */
public interface YamlSupplier extends Supplier<Yaml> {

}
