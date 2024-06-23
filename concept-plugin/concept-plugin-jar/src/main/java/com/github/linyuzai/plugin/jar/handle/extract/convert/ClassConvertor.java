package com.github.linyuzai.plugin.jar.handle.extract.convert;

import com.github.linyuzai.plugin.core.handle.extract.convert.AbstractPluginConvertor;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * byte[] 转 {@link InputStream} 的转换器
 */
public class ClassConvertor extends AbstractPluginConvertor<JarClass, Class<?>> {

    /**
     * 将所有的 byte[] 转为 {@link ByteArrayInputStream}
     *
     * @param jarClass value 类型为 byte[] 的 {@link Map}
     * @return value 类型为 {@link ByteArrayInputStream} 的 {@link Map}
     */
    @SneakyThrows
    @Override
    public Class<?> doConvert(JarClass jarClass) {
        return jarClass.get();
    }
}
