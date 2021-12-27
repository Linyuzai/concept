package com.github.linyuzai.download.core.source.reflect.conversion;

import com.github.linyuzai.download.core.exception.DownloadException;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ValueConversion {

    private static final ValueConversion helper = new ValueConversion(StringToCharsetValueConvertor.getInstance());

    public static ValueConversion helper() {
        return helper;
    }

    private final Collection<ValueConvertor> convertors = new CopyOnWriteArrayList<>();

    private ValueConversion(ValueConvertor... convertors) {
        register(convertors);
    }

    public void register(Collection<? extends ValueConvertor> convertors) {
        if (convertors == null) {
            throw new NullPointerException("Value convertors is null");
        }
        this.convertors.addAll(convertors);
    }

    public void register(ValueConvertor... convertors) {
        register(Arrays.asList(convertors));
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
