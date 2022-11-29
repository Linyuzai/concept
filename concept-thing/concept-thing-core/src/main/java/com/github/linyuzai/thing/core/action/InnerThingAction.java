package com.github.linyuzai.thing.core.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class InnerThingAction implements ThingAction {

    private final Supplier<ThingActionInvocation> supplier;
}
