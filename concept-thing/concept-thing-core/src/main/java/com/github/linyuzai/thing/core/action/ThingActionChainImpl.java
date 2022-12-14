package com.github.linyuzai.thing.core.action;

import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ThingActionChainImpl implements ThingActionChain {

    private final ThingContext context;

    private final List<ThingAction> actions = new ArrayList<>();

    @Override
    public ThingActionChain next(ThingAction action) {
        actions.add(action);
        return this;
    }

    @Override
    public ThingActionExecution execute() {
        List<ThingActionExecution> executions = new ArrayList<>();
        for (ThingAction action : actions) {
            ThingActionExecution execution = action.execute();
            executions.add(execution);
        }
        return new ThingActionExecutions(context, executions);
    }
}
