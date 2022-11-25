package com.github.linyuzai.thing.core.concept;

public interface Attribute {

    String getId();

    Label getLabel();

    Thing getThing();

    <T> T getValue();
}
