package com.github.linyuzai.attribute.dynamic.core.concept;

import com.github.linyuzai.attribute.dynamic.core.access.AttributeAccessor;
import com.github.linyuzai.attribute.dynamic.core.decode.AttributeValueDecoder;
import com.github.linyuzai.attribute.dynamic.core.encode.AttributeValueEncoder;
import com.github.linyuzai.attribute.dynamic.core.factory.AttributeFactory;
import com.github.linyuzai.attribute.dynamic.core.valid.AttributeValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class AbstractAttribute implements Attribute {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private Object value;

    @NonNull
    private final AttributeValueEncoder valueEncoder;

    @NonNull
    private final AttributeValueDecoder valueDecoder;

    private final List<AttributeAccessor> accessors;

    private final List<AttributeValidator> validators;

    private final AttributeFactory factory;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public boolean update(Object value) {
        Object decode = valueDecoder.decode(value);
        if (Objects.equals(decode, value)) {
            return false;
        }
        this.value = decode;
        return false;
    }
}
