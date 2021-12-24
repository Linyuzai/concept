package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.source.AbstractSource;
import lombok.*;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 文本下载源 / A source that holds a text
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextSource extends AbstractSource {

    protected String text;

    protected byte[] bytes;

    protected TextSource(@NonNull String text) {
        this.text = text;
        this.bytes = getBytes();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public String getContentType() {
        String contentType = super.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            setContentType(ContentType.Text.PLAIN);
        }
        return super.getContentType();
    }

    /**
     * @return 获得文本字节数 / Get bytes count of text
     */
    @Override
    public Long getLength() {
        return (long) bytes.length;
    }

    /**
     * @return 获得字节数组 / Get the bytes
     */
    public byte[] getBytes() {
        Charset charset = getCharset();
        return charset == null ? text.getBytes() : text.getBytes(charset);
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
