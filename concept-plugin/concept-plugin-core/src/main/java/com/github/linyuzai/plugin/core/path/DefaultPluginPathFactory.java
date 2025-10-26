package com.github.linyuzai.plugin.core.path;

/**
 * 默认插件路径工厂
 */
public class DefaultPluginPathFactory implements PluginPathFactory {

    @Override
    public String create(String parent, String name) {
        return parent + "!/" + name;
    }
}
