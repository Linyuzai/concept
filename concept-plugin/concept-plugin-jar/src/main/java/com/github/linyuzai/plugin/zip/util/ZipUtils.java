package com.github.linyuzai.plugin.zip.util;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.io.File;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.zip.ZipFile;

public class ZipUtils {

    public static final String SEPARATOR = "!/";

    public static File getFile(PluginDefinition definition, String... suffixes) {
        File file = new File(definition.getPath());
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        for (String suffix : suffixes) {
            if (file.getName().endsWith(suffix)) {
                return file;
            }
        }
        return null;
    }
}
