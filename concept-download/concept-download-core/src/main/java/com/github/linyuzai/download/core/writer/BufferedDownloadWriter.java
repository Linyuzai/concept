package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.concept.Downloadable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 使用缓冲区的写入器 / Writer using buffer
 * 默认缓冲区为1M / The default buffer is 1M
 */
@AllArgsConstructor
@NoArgsConstructor
public class BufferedDownloadWriter implements DownloadWriter {

    private int bufferSize = 1024 * 1024;

    /**
     * 返回true / Return true
     *
     * @param downloadable 可下载的资源 / Resource can be downloaded
     * @param range        写入的范围 / Range of writing
     * @param context      下载上下文 / Context of download
     * @return true
     */
    @Override
    public boolean support(Downloadable downloadable, Range range, DownloadContext context) {
        return true;
    }

    /**
     * 如果编码为null则使用字节流处理 / Byte stream is used if the charset is null
     * 如果有编码则使用字符流处理 / Char stream is used if the charset is existed
     *
     * @param is      输入流 / Input stream
     * @param os      输出流 / Output stream
     * @param range   写入的范围 / Range of writing
     * @param charset 编码 / Charset
     * @param length  总字节数，可能为0 / Total bytes count, may be 0
     * @throws IOException I/O exception
     */
    @Override
    public void write(InputStream is, OutputStream os, Range range, Charset charset, long length) throws IOException {
        if (charset == null /*|| length > 0 && bufferSize >= length*/) {
            int len;
            byte[] bytes = new byte[bufferSize];
            while ((len = is.read(bytes)) > 0) {
                os.write(bytes, 0, len);
            }
        } else {
            InputStreamReader isr = new InputStreamReader(is, charset);
            BufferedReader br = new BufferedReader(isr, bufferSize);
            int len;
            char[] chars = new char[bufferSize];
            char[] result = new char[0];
            while ((len = br.read(chars)) > 0) {
                result = concat(result, chars, len);
            }
            String string = new String(result);
            byte[] bytes = string.getBytes(charset);
            os.write(bytes);
        }
    }

    private char[] concat(char[] current, char[] append, int length) {
        char[] newChars = new char[current.length + length];
        System.arraycopy(current, 0, newChars, 0, current.length);
        System.arraycopy(append, 0, newChars, current.length, length);
        return newChars;
    }
}
