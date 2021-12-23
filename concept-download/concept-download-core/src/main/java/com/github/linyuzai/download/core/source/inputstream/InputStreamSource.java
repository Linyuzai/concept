package com.github.linyuzai.download.core.source.inputstream;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 持有一个输入流的下载源 / Source holds an input stream
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InputStreamSource extends AbstractSource {

    protected InputStream inputStream;

    @Override
    public Long getLength() {
        return null;
    }

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
                    writer.write(getInputStream(), os, range, getCharset(), getLength());
                }
            };
            handler.handle(part);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    public static class Builder extends AbstractSource.Builder<InputStreamSource, Builder> {

        private InputStream inputStream;

        public Builder inputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public InputStreamSource build() {
            return super.build(new InputStreamSource(inputStream));
        }
    }

    @Override
    public String toString() {
        return "InputStreamSource{" +
                "inputStream=" + inputStream +
                '}';
    }
}
