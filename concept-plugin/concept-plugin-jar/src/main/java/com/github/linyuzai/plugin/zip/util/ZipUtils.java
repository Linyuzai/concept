package com.github.linyuzai.plugin.zip.util;

import java.io.File;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.zip.ZipFile;

public class ZipUtils {

    public static final String SEPARATOR = "!/";

    public static File getFile(Object o, String... suffixes) {
        File file;
        if (o instanceof File) {
            file = (File) o;
        } else if (o instanceof String) {
            file = new File((String) o);
        } else {
            return null;
        }
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
