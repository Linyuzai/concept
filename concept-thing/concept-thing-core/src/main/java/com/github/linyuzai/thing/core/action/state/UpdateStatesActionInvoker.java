package com.github.linyuzai.thing.core.action.state;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionInvocation;
import com.github.linyuzai.thing.core.action.ThingActionInvoker;
import com.github.linyuzai.thing.core.concept.Thing;

public class UpdateStatesActionInvoker implements ThingActionInvoker {

    @Override
    public boolean support(ThingAction action, Thing thing) {
        return action instanceof UpdateStatesAction;
    }

    @Override
    public ThingActionInvocation invoke(ThingAction action, Thing thing) {
        UpdateStatesAction usa = (UpdateStatesAction) action;
        thing.getStates().update(usa.getStates());
        return null;
    }
}
