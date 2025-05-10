package com.github.linyuzai.plugin.zip.autoload;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

/**
 * 过滤 zip 文件
 */
public class ZipLocationFilter implements PluginLocation.Filter {

    @Override
    public boolean filter(String group, String name) {
        return name.endsWith(ZipPlugin.SUFFIX_ZIP);
    }
}
