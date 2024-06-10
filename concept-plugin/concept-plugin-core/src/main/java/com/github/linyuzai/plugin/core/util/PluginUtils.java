package com.github.linyuzai.plugin.core.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

public class PluginUtils {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Copies the content of a InputStream into an OutputStream
     *
     * @param is         the InputStream to copy
     * @param os         the target, may be null to simulate output to dev/null on Linux and NUL on Windows
     * @param bufferSize the buffer size to use, must be bigger than 0
     * @return the number of bytes copied
     * @throws IOException              if an error occurs
     * @throws IllegalArgumentException if bufferSize is smaller than or equal to 0
     */
    public static long copy(InputStream is, OutputStream os, int bufferSize) throws IOException {
        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize must be bigger than 0");
        }
        final byte[] buffer = new byte[bufferSize];
        int n;
        long count = 0;
        while (-1 != (n = is.read(buffer))) {
            if (os != null) {
                os.write(buffer, 0, n);
            }
            count += n;
        }
        return count;
    }

    public static byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copy(is, os, DEFAULT_BUFFER_SIZE);
        return os.toByteArray();
    }

    public static URL getURL(File file) throws MalformedURLException {
        return getURL(file.getAbsolutePath() + "!/");
    }

    private static URL getURL(String path) throws MalformedURLException {
        String file = path.replace("file:////", "file://"); // Fix UNC paths
        return new URL("jar", "", -1, file);
    }
}
