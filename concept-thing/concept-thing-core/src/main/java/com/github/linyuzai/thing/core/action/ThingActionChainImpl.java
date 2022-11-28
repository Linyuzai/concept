package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.concept.Thing;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ThingActionChainImpl implements ThingActionChain {

    private final Thing thing;

    private final List<ThingAction> actions = new ArrayList<>();

    @Override
    public ThingActionChain action(ThingAction action) {
        actions.add(action);
        return this;
    }

    @Override
    public void invoke() {
        List<ThingActionInvoker> invokers = thing.getContext().get(ThingActionInvoker.class);
        for (ThingAction action : actions) {
            for (ThingActionInvoker invoker : invokers) {
                if (invoker.support(action, thing)) {
                    ThingActionInvocation invocation = invoker.invoke(action, thing);
                    //TODO return invocation
                }
            }
        }
    }
}
