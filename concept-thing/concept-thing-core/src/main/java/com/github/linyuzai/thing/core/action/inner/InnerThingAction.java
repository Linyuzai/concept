package com.github.linyuzai.thing.core.action.inner;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class InnerThingAction implements ThingAction {

    private final ThingContext context;

    private final Supplier<ThingActionInvocation> supplier;
}
