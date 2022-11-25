package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractState implements State {

    private String id;

    private Label label;

    private Thing thing;

    private Object value;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        return (T) value;
    }
}
