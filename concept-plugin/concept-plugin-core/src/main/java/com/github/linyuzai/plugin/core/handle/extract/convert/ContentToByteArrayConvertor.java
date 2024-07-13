package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.SneakyThrows;

/**
 * 转 byte[]
 */
public class ContentToByteArrayConvertor extends AbstractPluginConvertor<Plugin.Content, byte[]> {

    @SneakyThrows
    @Override
    public byte[] doConvert(Plugin.Content content) {
        return PluginUtils.read(content.getInputStream());
    }
}
