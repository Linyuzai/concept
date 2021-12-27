package com.github.linyuzai.download.core.source.reflect.conversion;

import com.github.linyuzai.download.core.exception.DownloadException;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ValueConversion {

    private static final ValueConversion helper = new ValueConversion();

    public static ValueConversion helper() {
        return helper;
    }

    private final Collection<ValueConvertor> convertors = new CopyOnWriteArrayList<>();

    public void register(ValueConvertor convertor) {
        if (convertor == null) {
            return;
        }
        convertors.add(convertor);
    }

    public Object convert(Object value, Class<?> type) {
        if (value == null) {
            return null;
        }
        for (ValueConvertor convertor : convertors) {
            if (convertor.support(value, type)) {
                return convertor.convert(value);
            }
        }
        throw new DownloadException("Value " + value + " cannot convert to " + type);
    }
}
