package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.ReadUtils;
import lombok.*;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 转 {@link String}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentToStringConvertor extends AbstractPluginConvertor<Plugin.Content, String> {

    /**
     * 编码
     */
    private Charset charset;

    @SneakyThrows
    @Override
    public String doConvert(Plugin.Content content) {
        try (InputStream is = content.getInputStream()) {
            byte[] bytes = ReadUtils.read(is);
            return charset == null ? new String(bytes) : new String(bytes, charset);
        }
    }
}
