package com.github.linyuzai.attribute.dynamic.core.concept;

import com.github.linyuzai.attribute.dynamic.core.access.AttributeAccessor;
import com.github.linyuzai.attribute.dynamic.core.decode.AttributeValueDecoder;
import com.github.linyuzai.attribute.dynamic.core.encode.AttributeValueEncoder;
import com.github.linyuzai.attribute.dynamic.core.valid.AttributeValidator;
import com.github.linyuzai.attribute.dynamic.core.value.AttributeValueFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
@AllArgsConstructor
public class AbstractAttribute implements Attribute {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final AttributeValueFactory valueFactory;

    @NonNull
    private final AttributeValueEncoder valueEncoder;

    @NonNull
    private final AttributeValueDecoder valueDecoder;

    private final List<AttributeAccessor> accessors;

    private final List<AttributeValidator> validators;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        return (T) valueFactory.getValue();
    }
}
