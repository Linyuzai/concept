package com.github.linyuzai.attribute.dynamic.core.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DynamicSizeValueFactory implements AttributeValueFactory {

    private AttributeValueFactory valueFactory;

    private List<Object> values;

    @Override
    public Object getValue() {
        return values;
    }
}
