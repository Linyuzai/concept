package com.github.linyuzai.plugin.zip.metadata;

import com.github.linyuzai.plugin.core.metadata.AbstractPluginMetadataFinder;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

public abstract class ZipPluginMetadataFinder extends AbstractPluginMetadataFinder {

    protected String[] getSupportSuffixes() {
        return new String[]{ZipPlugin.SUFFIX_ZIP};
    }

}
