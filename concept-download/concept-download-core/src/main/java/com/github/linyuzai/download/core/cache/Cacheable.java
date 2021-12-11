package com.github.linyuzai.download.core.cache;

import java.io.File;

public interface Cacheable {

    String PATH = new File(System.getProperty("user.home"), "DownloadCache").getAbsolutePath();

    boolean isCacheEnabled();

    String getCachePath();

    default void deleteCache() {

    }
}
