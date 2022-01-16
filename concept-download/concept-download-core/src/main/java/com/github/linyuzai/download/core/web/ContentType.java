package com.github.linyuzai.download.core.web;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * Content type
 */
public class ContentType {

    public static class Application {
        public static final String OCTET_STREAM = "application/octet-stream";
        public static final String X_ZIP_COMPRESSED = "application/x-zip-compressed";
    }

    public static class Text {
        public static final String PLAIN = "text/plain";
    }

    /**
     * 获得文件的 Content Type / Get the content type of the file
     *
     * @param file 文件 / File
     * @return Content Type
     */
    public static String file(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            if (contentType != null) {
                return contentType;
            }
        } catch (Throwable ignore) {
        }
        try {
            String contentType = new MimetypesFileTypeMap().getContentType(file);
            if (contentType != null) {
                return contentType;
            }
        } catch (Throwable ignore) {

        }
        try {
            String contentType = URLConnection.getFileNameMap().getContentTypeFor(file.getAbsolutePath());
            if (contentType != null) {
                return contentType;
            }
        } catch (Throwable ignore) {

        }
        return null;
    }
}
