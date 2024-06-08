package com.github.linyuzai.plugin.core.util;

import com.github.linyuzai.plugin.jar.extension.NestedJarHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginUtils {

    public static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static byte[] read(InputStream is) throws IOException {
        return read(is, Integer.MAX_VALUE);
    }

    public static byte[] read(InputStream is, int len) throws IOException {
        if (len < 0) {
            throw new IllegalArgumentException("len < 0");
        }

        List<byte[]> bufs = null;
        byte[] result = null;
        int total = 0;
        int remaining = len;
        int n;
        do {
            byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
            int nread = 0;

            // read to EOF which may read more or less than buffer size
            while ((n = is.read(buf, nread,
                    Math.min(buf.length - nread, remaining))) > 0) {
                nread += n;
                remaining -= n;
            }

            if (nread > 0) {
                if (MAX_BUFFER_SIZE - total < nread) {
                    throw new OutOfMemoryError("Required array size too large");
                }
                if (nread < buf.length) {
                    buf = Arrays.copyOfRange(buf, 0, nread);
                }
                total += nread;
                if (result == null) {
                    result = buf;
                } else {
                    if (bufs == null) {
                        bufs = new ArrayList<>();
                        bufs.add(result);
                    }
                    bufs.add(buf);
                }
            }
            // if the last call to read returned -1 or the number of bytes
            // requested have been read then break
        } while (n >= 0 && remaining > 0);

        if (bufs == null) {
            if (result == null) {
                return new byte[0];
            }
            return result.length == total ?
                    result : Arrays.copyOf(result, total);
        }

        result = new byte[total];
        int offset = 0;
        remaining = total;
        for (byte[] b : bufs) {
            int count = Math.min(b.length, remaining);
            System.arraycopy(b, 0, result, offset, count);
            offset += count;
            remaining -= count;
        }

        return result;
    }

    public static URL getURL(File file) throws MalformedURLException {
        String path = file.getAbsolutePath() + "!/";
        path = path.replace("file:////", "file://"); // Fix UNC paths
        return new URL("jar", "", -1, path);
    }

    public static URL getURL(URL url, String name, URLStreamHandler handler) throws MalformedURLException {
        String file = url.toString() + name + "!/";
        file = file.replace("file:////", "file://"); // Fix UNC paths
        return new URL("jar", "", -1, file, handler);
    }
}
