package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * byte[] 转 {@link InputStream} 的转换器
 */
public class ContentToByteBufferConvertor extends AbstractPluginConvertor<Plugin.Content, ByteBuffer> {

    /**
     * 将所有的 byte[] 转为 {@link ByteArrayInputStream}
     *
     * @param content value 类型为 byte[] 的 {@link Map}
     * @return value 类型为 {@link ByteArrayInputStream} 的 {@link Map}
     */
    @SneakyThrows
    @Override
    public ByteBuffer doConvert(Plugin.Content content) {
        return ByteBuffer.wrap(PluginUtils.read(content.getInputStream()));
    }
}