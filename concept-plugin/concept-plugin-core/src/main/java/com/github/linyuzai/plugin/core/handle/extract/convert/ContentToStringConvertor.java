package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.*;

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
        byte[] bytes = PluginUtils.read(content.getInputStream());
        return charset == null ? new String(bytes) : new String(bytes, charset);
    }
}
