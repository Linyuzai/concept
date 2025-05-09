package com.github.linyuzai.plugin.jar.metadata;

import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.zip.metadata.ZipFilePluginMetadataFinder;

import java.util.Arrays;
import java.util.stream.Stream;

public class JarFilePluginMetadataFinder extends ZipFilePluginMetadataFinder {

    @Override
    protected String[] getSupportSuffixes() {
        return Stream.concat(
                        Arrays.stream(super.getSupportSuffixes()),
                        Arrays.stream(new String[]{JarPlugin.SUFFIX_ZIP}))
                .toArray(String[]::new);
    }
}
