package com.github.linyuzai.download.core.source.reflect.conversion;

import com.github.linyuzai.download.core.exception.DownloadException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ValueConversion {

    private static final ValueConversion helper = new ValueConversion();

    public static ValueConversion helper() {
        return helper;
    }

    private final Map<Class<? extends ValueConvertor>, ValueConvertor> convertorMap = new ConcurrentHashMap<>();

    public void register(ValueConvertor convertor) {
        if (convertor == null) {
            return;
        }
        convertorMap.put(convertor.getClass(), convertor);
    }

    public Object convert(Class<? extends ValueConvertor> clazz, Object value) throws Throwable {
        ValueConvertor convertor = convertorMap.computeIfAbsent(clazz, this::newInstance);
        return convertor.convert(value);
    }

    public ValueConvertor newInstance(Class<? extends ValueConvertor> clazz) {
        try {
            return clazz.newInstance();
        } catch (Throwable e) {
            throw new DownloadException(e);
        }
    }

    public Object convert(Object value, Class<?> type) {
        if (value == null) {
            return null;
        }
        Collection<ValueConvertor> convertors = convertorMap.values();
        for (ValueConvertor convertor : convertors) {
            if (convertor.support(value, type)) {
                return convertor.convert(value);
            }
        }
        throw new DownloadException("Value " + value + " cannot convert to " + type);
    }
}
