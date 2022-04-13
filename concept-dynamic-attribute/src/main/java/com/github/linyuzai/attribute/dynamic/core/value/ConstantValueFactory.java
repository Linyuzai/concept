package com.github.linyuzai.attribute.dynamic.core.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConstantValueFactory implements AttributeValueFactory {

    private Object value;
}
