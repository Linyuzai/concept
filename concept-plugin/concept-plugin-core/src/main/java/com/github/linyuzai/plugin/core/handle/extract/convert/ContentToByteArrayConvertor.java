package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.ReadUtils;
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
            return ReadUtils.read(is);
        }
    }
}
