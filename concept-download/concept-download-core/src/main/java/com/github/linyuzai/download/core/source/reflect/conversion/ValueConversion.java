package com.github.linyuzai.download.core.source.reflect.conversion;

import com.github.linyuzai.download.core.exception.DownloadException;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 值类型转换的帮助类。
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
     * 注册转换器。
     *
     * @param convertors 转换器
     */
    public void register(Collection<? extends ValueConvertor> convertors) {
        if (convertors == null) {
            throw new NullPointerException("Value convertors is null");
        }
        this.convertors.addAll(convertors);
    }

    /**
     * 注册转换器
     *
     * @param convertors 转换器
     */
    public void register(ValueConvertor... convertors) {
        register(Arrays.asList(convertors));
    }

    /**
     * 转换值。
     *
     * @param value 需要转换的值
     * @param type  目标类型
     * @return 转换后的值
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
        throw new DownloadException("Value " + value + " cannot format to " + type);
    }
}
