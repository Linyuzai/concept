package com.github.linyuzai.plugin.zip.util;

import java.io.File;
import java.util.zip.ZipFile;

public class ZipUtils {

    public static final String SEPARATOR = "!/";

    public static final String SUFFIX = ".zip";

    public static ZipFile getZipFile(Object source) {
        File file = getFile(source, SUFFIX);
        if (file == null) {
            return null;
        }
        try {
            return new ZipFile(file);
        } catch (Throwable ignore) {
        }
        return null;
    }

    public static File getFile(Object o, String suffix) {
        File file;
        if (o instanceof File) {
            file = (File) o;
        } else if (o instanceof String) {
            file = new File((String) o);
        } else {
            file = null;
        }
        if (file != null &&
                file.exists() &&
                file.isFile() &&
                file.getName().endsWith(suffix)) {
            return file;
        }
        return null;
    }
}
