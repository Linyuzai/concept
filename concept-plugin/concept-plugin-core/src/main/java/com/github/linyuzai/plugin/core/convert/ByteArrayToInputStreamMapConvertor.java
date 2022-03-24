package com.github.linyuzai.plugin.core.convert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class ByteArrayToInputStreamMapConvertor extends AbstractPluginConvertor<Map<?, byte[]>, Map<Object, InputStream>> {

    @Override
    public Map<Object, InputStream> doConvert(Map<?, byte[]> source) {
        Map<Object, InputStream> map = new LinkedHashMap<>();
        for (Map.Entry<?, byte[]> entry : source.entrySet()) {
            map.put(entry.getKey(), new ByteArrayInputStream(entry.getValue()));
        }
        return map;
    }
}
