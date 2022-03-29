package com.github.linyuzai.plugin.core.convert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * byte[] 转 {@link String} 的转换器
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ByteArrayToStringMapConvertor extends AbstractPluginConvertor<Map<?, byte[]>, Map<Object, String>> {

    /**
     * 编码
     */
    private Charset charset;

    public ByteArrayToStringMapConvertor(String charset) {
        this.charset = Charset.forName(charset);
    }

    @Override
    public Map<Object, String> doConvert(Map<?, byte[]> source) {
        Map<Object, String> map = new LinkedHashMap<>();
        for (Map.Entry<?, byte[]> entry : source.entrySet()) {
            String s = charset == null ? new String(entry.getValue()) : new String(entry.getValue(), charset);
            map.put(entry.getKey(), s);
        }
        return map;
    }
}
