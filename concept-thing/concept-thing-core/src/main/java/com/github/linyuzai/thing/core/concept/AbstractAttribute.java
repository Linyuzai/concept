package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractAttribute implements Attribute, Attribute.Modifiable {

    private String id;

    private String key;

    private Label label;

    private Thing thing;

    private Object value;

    @Override
    public void update(Object value) {
        //TODO valid
        setValue(value);
    }
}
