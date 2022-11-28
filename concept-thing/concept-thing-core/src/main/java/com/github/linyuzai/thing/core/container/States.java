package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.State;

import java.util.Map;

public interface States extends Container<State>, Container.Modifiable<State> {

    void update(Map<String, Object> values);
}
