package com.github.linyuzai.thing.core.action.state;

import com.github.linyuzai.thing.core.action.ThingAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class UpdateStatesAction implements ThingAction {

    private final Map<String, Object> states;
}
