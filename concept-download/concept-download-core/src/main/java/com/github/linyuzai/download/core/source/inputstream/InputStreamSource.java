package com.github.linyuzai.download.core.source.inputstream;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 持有一个输入流的下载源 / Source holds an input stream
 */
@AllArgsConstructor
public class InputStreamSource extends AbstractSource {

    private InputStream inputStream;

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        try (InputStream is = inputStream) {
            Part part = new Part() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return is;
                }

                @Override
                public String getName() {
                    return InputStreamSource.this.getName();
                }

                @Override
                public String getPath() {
                    return InputStreamSource.this.getName();
                }

                @Override
                public Charset getCharset() {
                    return InputStreamSource.this.getCharset();
                }

                @Override
                public void write() throws IOException {
                    writer.write(getInputStream(), os, range, getCharset(), 0);
                }
            };
            handler.handle(part);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }
}
