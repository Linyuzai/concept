package com.github.linyuzai.plugin.core.path;

public class DefaultPluginPathFactory implements PluginPathFactory {

    @Override
    public String create(String parent, String name) {
        return parent + "!/" + name;
    }
}
