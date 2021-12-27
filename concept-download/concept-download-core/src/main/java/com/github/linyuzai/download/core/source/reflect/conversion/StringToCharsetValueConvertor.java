package com.github.linyuzai.download.core.source.reflect.conversion;

import java.nio.charset.Charset;

public class StringToCharsetValueConvertor implements ValueConvertor<String, Charset> {

    private static final StringToCharsetValueConvertor instance = new StringToCharsetValueConvertor();

    public static StringToCharsetValueConvertor getInstance() {
        return instance;
    }

    static {
        ValueConversion.helper().register(getInstance());
    }

    @Override
    public Charset convert(String value) {
        return Charset.forName(value);
    }
}
