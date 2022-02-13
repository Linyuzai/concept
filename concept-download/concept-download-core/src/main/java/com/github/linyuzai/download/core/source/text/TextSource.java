package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.ContentType;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 支持一段文本的 {@link Source}。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextSource extends AbstractSource {

    /**
     * 文本内容
     */
    @NonNull
    @Setter(AccessLevel.PROTECTED)
    protected String text;

    /**
     * 文本对应的字节数组的缓存。
     */
    protected byte[] bytes;

    /**
     * 文本对应字节数组的 {@link ByteArrayInputStream}。
     *
     * @return {@link ByteArrayInputStream}
     */
    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(getBytes());
    }

    /**
     * 如果未指定 Content-Type 则返回 {@link ContentType.Text#PLAIN}。
     *
     * @return 指定的 Content-Type 或 {@link ContentType.Text#PLAIN}
     */
    @Override
    public String getContentType() {
        String contentType = super.getContentType();
        if (!StringUtils.hasText(contentType)) {
            setContentType(ContentType.Text.PLAIN);
        }
        return super.getContentType();
    }

    /**
     * 获得文本字节数。
     *
     * @return 文本字节数
     */
    @Override
    public Long getLength() {
        return (long) getBytes().length;
    }

    /**
     * 根据 {@link Charset} 获得字节数组。
     *
     * @return 字节数组
     */
    public byte[] getBytes() {
        if (bytes == null) {
            Charset charset = getCharset();
            bytes = charset == null ? text.getBytes() : text.getBytes(charset);
        }
        return bytes;
    }

    /**
     * 直接返回 true。
     *
     * @return true
     */
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String getDescription() {
        return "TextSource(" + text + ")";
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
