package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.operation.Operation;
import com.github.linyuzai.thing.core.operation.StateUpdateOperation;

public abstract class AbstractState implements State {

    protected String id;

    protected Label label;

    protected Thing thing;

    protected Object value;

    @Override
    public String id() {
        return id;
    }

    @Override
    public Label label() {
        return label;
    }

    @Override
    public Thing thing() {
        return thing;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T value() {
        return (T) value;
    }

    @Override
    public Operation update(Object value) {
        Operation operation = new StateUpdateOperation(this, this.value, value);
        this.value = value;
        return operation;
    }
}
