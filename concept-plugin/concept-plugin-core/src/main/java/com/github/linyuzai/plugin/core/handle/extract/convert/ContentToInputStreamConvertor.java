package com.github.linyuzai.plugin.core.handle.extract.convert;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.SneakyThrows;

import java.io.InputStream;

/**
 * 转 {@link InputStream}
 */
public class ContentToInputStreamConvertor extends AbstractPluginConvertor<Plugin.Content, InputStream> {

    @SneakyThrows
    @Override
    public InputStream doConvert(Plugin.Content content) {
        return content.getInputStream();
    }
}
