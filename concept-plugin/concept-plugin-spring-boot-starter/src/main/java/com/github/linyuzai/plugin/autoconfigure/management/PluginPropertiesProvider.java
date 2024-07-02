package com.github.linyuzai.plugin.autoconfigure.management;

import lombok.Data;

import java.util.List;

public interface PluginPropertiesProvider {

    List<PluginPropertyEntry> getProperties(String group, String name);

    @Data
    class PluginPropertyEntry {

        private String name;

        private String value;
    }
}
