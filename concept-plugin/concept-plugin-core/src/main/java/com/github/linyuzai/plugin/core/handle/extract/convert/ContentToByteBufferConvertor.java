package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;

/**
 * 转 {@link ByteBuffer}
 */
public class ContentToByteBufferConvertor extends AbstractPluginConvertor<Plugin.Content, ByteBuffer> {

    @SneakyThrows
    @Override
    public ByteBuffer doConvert(Plugin.Content content) {
        return ByteBuffer.wrap(PluginUtils.read(content.getInputStream()));
    }
}
