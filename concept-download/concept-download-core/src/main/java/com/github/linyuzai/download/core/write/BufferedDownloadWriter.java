package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.web.Range;
import lombok.*;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 使用缓冲区的写入器 / Writer using buffer
 * 默认缓冲区为1M / The default buffer is 1M
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BufferedDownloadWriter implements DownloadWriter {

    private int minBufferSize = 1024 * 1024;

    private int maxBufferSize = 8 * 1024 * 1024;

    /**
     * 返回true / Return true
     *
     * @param resource 可下载的资源 / Resource can be downloaded
     * @param context  下载上下文 / Context of download
     * @return true
     */
    @Override
    public boolean support(Resource resource, DownloadContext context) {
        return true;
    }

    /**
     * 如果编码为 null 则使用字节流处理 / Byte stream is used if the charset is null
     * 如果有编码则使用字符流处理 / Char stream is used if the charset is existed
     *
     * @param is      输入流 / Input stream
     * @param os      输出流 / Output stream
     * @param range   写入的范围 / Range of writing
     * @param charset 编码 / Charset
     * @param length  总字节数，可能为0 / Total bytes count, may be 0
     */
    @Override
    public void write(InputStream is, OutputStream os, Range range, Charset charset, Long length, Callback callback) throws IOException {
        if (charset == null /*|| length > 0 && bufferSize >= length*/) {
            if (range == null) {
                write0(is, os, callback);
            } else {
                if (range.hasStart()) {
                    long skip = is.skip(range.getStart());
                }
                if (range.hasEnd()) {
                    long total = 0;
                    long l = range.getLength();
                    long current = 0;
                    byte[] bytes = new byte[minBufferSize];
                    int len;
                    while ((len = is.read(bytes)) > 0) {
                        if (total + len > l) {
                            long increase = l - total;
                            os.write(bytes, 0, (int) increase);
                            current += increase;
                            if (callback != null) {
                                callback.onWrite(current, increase);
                            }
                            break;
                        } else {
                            os.write(bytes, 0, len);
                            total += len;
                            current += len;
                            if (callback != null) {
                                callback.onWrite(current, len);
                            }
                        }
                        bytes = getBuffer(bytes, len);
                    }
                } else {
                    write0(is, os, callback);
                }
            }
        } else {
            InputStreamReader isr = new InputStreamReader(is, charset);
            BufferedReader br = new BufferedReader(isr, minBufferSize);
            int len;
            char[] chars = new char[minBufferSize];
            char[] result = new char[0];
            while ((len = br.read(chars)) > 0) {
                result = concat(result, chars, len);
            }
            String string = new String(result);
            byte[] bytes = string.getBytes(charset);
            if (callback != null) {
                callback.onWrite(bytes.length, bytes.length);
            }
            os.write(bytes);
        }
    }

    private void write0(InputStream is, OutputStream os, Callback callback) throws IOException {
        long current = 0;
        byte[] bytes = new byte[minBufferSize];
        int len;
        while ((len = is.read(bytes)) > 0) {
            os.write(bytes, 0, len);
            current += len;
            if (callback != null) {
                callback.onWrite(current, len);
            }
            bytes = getBuffer(bytes, len);
        }
    }

    private byte[] getBuffer(byte[] buffer, int len) {
        int size = buffer.length;
        if (size == maxBufferSize) {
            return buffer;
        }
        if (len == size) {
            int newSize = size * 2;
            if (newSize > maxBufferSize) {
                return new byte[maxBufferSize];
            } else {
                return new byte[newSize];
            }
        } else {
            return buffer;
        }
    }

    private char[] concat(char[] current, char[] append, int length) {
        char[] newChars = new char[current.length + length];
        System.arraycopy(current, 0, newChars, 0, current.length);
        System.arraycopy(append, 0, newChars, current.length, length);
        return newChars;
    }
}
