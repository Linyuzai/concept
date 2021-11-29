package com.github.linyuzai.download.autoconfigure.info;

public class DownloadConceptInfo {

    public static final String PREFIX = "Concept download >> ";

    public static String wrapper(String msg) {
        return PREFIX + msg;
    }
}
