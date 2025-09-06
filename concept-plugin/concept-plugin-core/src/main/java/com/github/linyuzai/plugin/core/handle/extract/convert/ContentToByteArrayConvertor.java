package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.SneakyThrows;

import java.io.InputStream;

/**
 * 转 byte[]
 */
public class ContentToByteArrayConvertor extends AbstractPluginConvertor<Plugin.Content, byte[]> {

    @SneakyThrows
    @Override
    public byte[] doConvert(Plugin.Content content) {
        try (InputStream is = content.getInputStream()) {
            return PluginStorage.read(is);
        }
    }
}
