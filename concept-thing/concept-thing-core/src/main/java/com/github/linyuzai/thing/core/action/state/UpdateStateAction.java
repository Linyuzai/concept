package com.github.linyuzai.thing.core.action.state;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.State;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.select.ThingSelector;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateStateAction implements ThingAction {

    private final String stateId;

    private final Object value;
}
