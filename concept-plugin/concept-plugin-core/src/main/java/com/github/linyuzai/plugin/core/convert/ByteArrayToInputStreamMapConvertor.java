package com.github.linyuzai.plugin.core.convert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * byte[] 转 {@link InputStream} 的转换器
 */
public class ByteArrayToInputStreamMapConvertor extends AbstractPluginConvertor<Map<?, byte[]>, Map<Object, InputStream>> {

    /**
     * 将所有的 byte[] 转为 {@link ByteArrayInputStream}
     *
     * @param source value 类型为 byte[] 的 {@link Map}
     * @return value 类型为 {@link ByteArrayInputStream} 的 {@link Map}
     */
    @Override
    public Map<Object, InputStream> doConvert(Map<?, byte[]> source) {
        Map<Object, InputStream> map = new LinkedHashMap<>();
        for (Map.Entry<?, byte[]> entry : source.entrySet()) {
            map.put(entry.getKey(), new ByteArrayInputStream(entry.getValue()));
        }
        return map;
    }
}
