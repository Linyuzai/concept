package com.github.linyuzai.thing.core.action.state;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.action.ThingActionInvoker;
import com.github.linyuzai.thing.core.concept.State;
import com.github.linyuzai.thing.core.concept.Thing;

public class UpdateStateActionInvoker implements ThingActionInvoker {

    @Override
    public boolean support(ThingAction action, Thing thing) {
        return action instanceof UpdateStateAction;
    }

    @Override
    public ThingActionInvocation invoke(ThingAction action, Thing thing) {
        UpdateStateAction usa = (UpdateStateAction) action;
        State state = thing.getStates().get(usa.getStateId());
        if (state == null) {
            throw new NullPointerException();
        }
        state.update(usa.getValue());
        return null;
    }
}
