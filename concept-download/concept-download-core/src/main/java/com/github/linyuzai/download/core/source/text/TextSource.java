package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.source.AbstractSource;
import lombok.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 文本下载源 / A source that holds a text
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextSource extends AbstractSource {

    @NonNull
    @Setter
    protected String text;

    protected byte[] bytes;

    protected InputStream bytesInputStream;

    @Override
    public InputStream getInputStream() {
        return open();
    }

    private InputStream open() {
        if (bytesInputStream == null) {
            bytesInputStream = new ByteArrayInputStream(getBytes());
        }
        return bytesInputStream;
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
        return (long) getBytes().length;
    }

    /**
     * @return 获得字节数组 / Get the bytes
     */
    public byte[] getBytes() {
        if (bytes == null) {
            Charset charset = getCharset();
            bytes = charset == null ? text.getBytes() : text.getBytes(charset);
        }
        return bytes;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public void release() {
        if (bytesInputStream != null) {
            try {
                bytesInputStream.close();
            } catch (Throwable ignore) {
            }
        }
        bytesInputStream = null;
    }

    @Override
    public String toString() {
        return "TextSource{" +
                "text='" + text + '\'' +
                '}';
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends TextSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        private String text;

        public B text(String text) {
            this.text = text;
            return (B) this;
        }

        @Override
        protected T build(T target) {
            target.setText(text);
            return super.build(target);
        }

        @Override
        public T build() {
            return build((T) new TextSource());
        }
    }
}
