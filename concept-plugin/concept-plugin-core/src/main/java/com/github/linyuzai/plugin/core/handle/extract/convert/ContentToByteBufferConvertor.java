package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * 转 {@link ByteBuffer}
 */
public class ContentToByteBufferConvertor extends AbstractPluginConvertor<Plugin.Content, ByteBuffer> {

    @SneakyThrows
    @Override
    public ByteBuffer doConvert(Plugin.Content content) {
        try (InputStream is = content.getInputStream()) {
            return ByteBuffer.wrap(PluginStorage.read(is));
        }
    }
}
