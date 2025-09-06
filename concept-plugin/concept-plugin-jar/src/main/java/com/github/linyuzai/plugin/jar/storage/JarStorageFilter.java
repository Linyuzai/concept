package com.github.linyuzai.plugin.jar.storage;

import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.zip.storage.ZipStorageFilter;

/**
 * 过滤 jar 和 zip
 */
public class JarStorageFilter extends ZipStorageFilter {

    @Override
    public boolean filter(String group, String name) {
        return name.endsWith(JarPlugin.SUFFIX_JAR) || super.filter(group, name);
    }
}
