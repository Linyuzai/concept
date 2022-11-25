package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.operation.AttributeUpdateOperation;
import com.github.linyuzai.thing.core.operation.Operation;

public abstract class AbstractAttribute implements Attribute {

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
        Operation operation = new AttributeUpdateOperation(this, this.value, value);
        this.value = value;
        return operation;
    }
}
