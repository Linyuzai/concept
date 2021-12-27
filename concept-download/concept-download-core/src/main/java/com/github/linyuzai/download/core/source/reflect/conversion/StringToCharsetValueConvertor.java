package com.github.linyuzai.download.core.source.reflect.conversion;

import java.nio.charset.Charset;

/**
 * 将String转为Charset的转换器 / Converter for converting String to Charset
 */
public class StringToCharsetValueConvertor implements ValueConvertor<String, Charset> {

    private static final StringToCharsetValueConvertor instance = new StringToCharsetValueConvertor();

    public static StringToCharsetValueConvertor getInstance() {
        return instance;
    }

    @Override
    public Charset convert(String value) {
        return Charset.forName(value);
    }
}
