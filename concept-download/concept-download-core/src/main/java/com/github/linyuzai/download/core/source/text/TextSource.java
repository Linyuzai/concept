package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.*;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 文本下载源 / A source that holds a text
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TextSource extends AbstractSource {

    @NonNull
    protected String text;

    /**
     * @return 获得文本字节数 / Get bytes count of text
     */
    @Override
    public long getLength() {
        return getBytes().length;
    }

    /**
     * @return 获得字节数组 / Get the bytes
     */
    public byte[] getBytes() {
        Charset charset = getCharset();
        return charset == null ? text.getBytes() : text.getBytes(charset);
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        byte[] bytes = getBytes();
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            Part part = new Part() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return is;
                }

                @Override
                public String getName() {
                    return TextSource.this.getName();
                }

                @Override
                public String getPath() {
                    return TextSource.this.getName();
                }

                @Override
                public Charset getCharset() {
                    return TextSource.this.getCharset();
                }

                @Override
                public void write() throws IOException {
                    writer.write(getInputStream(), os, range, getCharset(), bytes.length);
                }
            };
            handler.handle(part);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString() {
        return "TextSource{" +
                "text='" + text + '\'' +
                '}';
    }

    public static class Builder extends AbstractSource.Builder<TextSource, Builder> {

        private String text;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public TextSource build() {
            return super.build(new TextSource(text));
        }
    }
}
