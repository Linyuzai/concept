package com.github.linyuzai.thing.core.concept;

public class AbstractLabel implements Label {

    protected String id;

    protected String name;

    protected Category category;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Category category() {
        return category;
    }
}
