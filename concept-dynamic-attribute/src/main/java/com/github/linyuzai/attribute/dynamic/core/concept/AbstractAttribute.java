package com.github.linyuzai.attribute.dynamic.core.concept;

import com.github.linyuzai.attribute.dynamic.core.access.AttributeAccessor;
import com.github.linyuzai.attribute.dynamic.core.valid.AttributeValidator;
import com.github.linyuzai.attribute.dynamic.core.value.AttributeValueFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AbstractAttribute implements Attribute {

    private final String id;

    private final String name;

    private final AttributeValueFactory valueFactory;

    private final List<AttributeAccessor> accessors;

    private final List<AttributeValidator> validators;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        return (T) valueFactory.getValue();
    }
}
