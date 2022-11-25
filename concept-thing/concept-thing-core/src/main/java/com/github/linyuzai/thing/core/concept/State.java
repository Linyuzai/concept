package com.github.linyuzai.thing.core.concept;

public interface State {

    String getId();

    Label getLabel();

    Thing getThing();

    <T> T getValue();
}
