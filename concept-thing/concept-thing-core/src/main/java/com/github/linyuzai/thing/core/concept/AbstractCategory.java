package com.github.linyuzai.thing.core.concept;

public abstract class AbstractCategory implements Category {

    protected String id;

    protected String name;

    protected Category category;

    protected Categories categories;

    protected Labels labels;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Category parent() {
        return category;
    }

    @Override
    public Category category(String id) {
        return categories.find(id);
    }

    @Override
    public Label label(String id) {
        return labels.find(id);
    }

    @Override
    public Categories categories() {
        return categories;
    }

    @Override
    public Labels labels() {
        return labels;
    }
}
