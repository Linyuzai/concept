package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;

@AllArgsConstructor
@NoArgsConstructor
public class BufferedSourceWriter implements SourceWriter {

    private int bufferSize = 1024 * 1024;

    @Override
    public boolean support(Source source, Range range, DownloadContext context) {
        return true;
    }

    @Override
    public void write(InputStream is, OutputStream os, Range range, Charset charset, long length) throws IOException {
        if (charset == null || length > 0 && bufferSize >= length) {
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
