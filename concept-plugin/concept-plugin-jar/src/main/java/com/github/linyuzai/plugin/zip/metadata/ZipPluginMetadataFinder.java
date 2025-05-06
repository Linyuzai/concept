package com.github.linyuzai.plugin.zip.metadata;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.AbstractPluginMetadataFinder;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PropertiesMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.util.ZipUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class ZipPluginMetadataFinder extends AbstractPluginMetadataFinder {

    protected String[] getSupportSuffixes() {
        return new String[]{ZipPlugin.SUFFIX};
    }

}
