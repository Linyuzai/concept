package com.github.linyuzai.download.core.cache;

import lombok.Getter;

import java.io.File;

@Getter
public class UserHomeDownloadCacheLocation implements DownloadCacheLocation {

    public static final String USER_HOME = System.getProperty("user.home");

    private final String path;

    public UserHomeDownloadCacheLocation() {
        path = new File(USER_HOME, "DownloadCache").getAbsolutePath();
    }
}
