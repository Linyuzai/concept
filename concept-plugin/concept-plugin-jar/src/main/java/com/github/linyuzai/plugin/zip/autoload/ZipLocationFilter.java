package com.github.linyuzai.plugin.zip.autoload;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;

public class ZipLocationFilter implements PluginLocation.Filter {

    @Override
    public boolean filter(String group, String name) {
        return name.endsWith(".zip");
    }
}
