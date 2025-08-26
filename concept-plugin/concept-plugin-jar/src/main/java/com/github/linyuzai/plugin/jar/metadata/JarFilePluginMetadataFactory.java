package com.github.linyuzai.plugin.jar.metadata;

import com.github.linyuzai.plugin.jar.concept.JarPluginSuffixes;
import com.github.linyuzai.plugin.zip.metadata.ZipFilePluginMetadataFactory;

public class JarFilePluginMetadataFactory extends ZipFilePluginMetadataFactory {

    @Override
    protected String[] getSupportSuffixes() {
        return JarPluginSuffixes.SUPPORTS_SUFFIXES;
    }
}
