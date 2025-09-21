package com.github.linyuzai.plugin.core.util;

import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadUtils {

    @Getter
    @Setter
    private static int bufferSize = 8192;

    @Getter
    @Setter
    private static long readLimit = -1;

    private ReadUtils() {
    }

    public static byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize < 1");
        }
        byte[] buffer = new byte[bufferSize];
        int n;
        long count = 0;
        while (-1 != (n = is.read(buffer))) {
            os.write(buffer, 0, n);
            count += n;
            if (readLimit > 0 && count > readLimit) {
                throw new IllegalArgumentException("Read limit exceeded:" + readLimit);
            }
        }
        return os.toByteArray();
    }
}
