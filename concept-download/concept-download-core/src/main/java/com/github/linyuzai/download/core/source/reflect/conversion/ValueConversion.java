package com.github.linyuzai.download.core.source.reflect.conversion;

import com.github.linyuzai.download.core.exception.DownloadException;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 将值做类型转换 / Convert the value use specific type
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ValueConversion {

    private static final ValueConversion instance = new ValueConversion(StringToCharsetValueConvertor.getInstance());

    public static ValueConversion getInstance() {
        return instance;
    }

    private final Collection<ValueConvertor> convertors = new CopyOnWriteArrayList<>();

    private ValueConversion(ValueConvertor... convertors) {
        register(convertors);
    }

    /**
     * 注册转换器 / Register converters
     *
     * @param convertors 转换器 / Convertors
     */
    public void register(Collection<? extends ValueConvertor> convertors) {
        if (convertors == null) {
            throw new NullPointerException("Value convertors is null");
        }
        this.convertors.addAll(convertors);
    }

    /**
     * 注册转换器 / Register converters
     *
     * @param convertors 转换器 / Convertors
     */
    public void register(ValueConvertor... convertors) {
        register(Arrays.asList(convertors));
    }

    /**
     * 转换值 / Convert value
     *
     * @param value 需要转换的值 / Value to convert
     * @param type  目标类型 / Target type
     * @return 转换后的值 / Converted value
     */
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
