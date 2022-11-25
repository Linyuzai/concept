package com.github.linyuzai.thing.core.concept;

public abstract class AbstractThing implements Thing {

    protected String id;

    protected String name;

    protected Categories categories;

    protected Attributes attributes;

    protected States states;

    protected Relationships relationships;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Category category(String id) {
        return categories.find(id);
    }

    @Override
    public Attribute attribute(String id) {
        return attributes.find(id);
    }

    @Override
    public State state(String id) {
        return states.find(id);
    }

    @Override
    public Categories categories() {
        return categories;
    }

    @Override
    public Attributes attributes() {
        return attributes;
    }

    @Override
    public States states() {
        return states;
    }

    @Override
    public Relationships relationships() {
        return relationships;
    }

    @Override
    public <T> T create(Class<T> target) {
        return null;
    }
}
