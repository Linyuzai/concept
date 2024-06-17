package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.*;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * byte[] 转 {@link String} 的转换器
 */
@Getter
@RequiredArgsConstructor
public class ContentToStringConvertor extends AbstractPluginConvertor<Plugin.Content, String> {

    /**
     * 编码
     */
    private final Charset charset;

    public ContentToStringConvertor() {
        this.charset = null;
    }

    public ContentToStringConvertor(String charset) {
        this.charset = Charset.forName(charset);
    }

    /**
     * 将所有的 byte[] 转为 {@link String}
     *
     * @param content value 类型为 byte[] 的 {@link Map}
     * @return value 类型为 {@link String} 的 {@link Map}
     */
    @SneakyThrows
    @Override
    public String doConvert(Plugin.Content content) {
        byte[] bytes = PluginUtils.read(content.getInputStream());
        return charset == null ? new String(bytes) : new String(bytes, charset);
    }
}
