package com.github.linyuzai.thing.core.operation;

import com.github.linyuzai.thing.core.concept.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StateUpdateOperation extends AbstractOperation {

    private final State state;

    private final Object oldValue;

    private final Object newValue;
}
