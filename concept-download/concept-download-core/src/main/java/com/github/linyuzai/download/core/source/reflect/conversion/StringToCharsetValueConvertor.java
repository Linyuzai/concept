package com.github.linyuzai.download.core.source.reflect.conversion;

import java.nio.charset.Charset;

/**
 * 将 {@link String} 转为 {@link Charset} 的 {@link ValueConvertor}。
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
