package com.github.linyuzai.plugin.jar.metadata;

import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginSuffixes;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.metadata.ZipFilePluginMetadataFactory;

import java.util.Arrays;
import java.util.stream.Stream;

public class JarFilePluginMetadataFactory extends ZipFilePluginMetadataFactory {

    @Override
    protected String[] getSupportSuffixes() {
        return JarPluginSuffixes.SUPPORTS_SUFFIXES;
    }
}
