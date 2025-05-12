package com.github.linyuzai.plugin.zip.storage;

import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

/**
 * 过滤 zip 文件
 */
public class ZipStorageFilter implements PluginStorage.Filter {

    @Override
    public boolean filter(String group, String name) {
        return name.endsWith(ZipPlugin.SUFFIX_ZIP);
    }
}
