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

    private final List<ThingActionInvoker> invokers;

    private final List<ThingAction> actions = new ArrayList<>();

    @Override
    public ThingActionChain next(ThingAction action) {
        actions.add(action);
        return this;
    }

    @Override
    public ThingActionInvocation invoke() {
        List<ThingAction> actionList = confirmActionsAndInvalid();
        List<ThingActionInvocation> invocations = new ArrayList<>();
        for (ThingAction action : actionList) {
            List<ThingActionInvoker> supportInvokers = getSupportInvokers(action);
            for (ThingActionInvoker invoker : supportInvokers) {
                ThingActionInvocation invocation = invoker.invoke(action, context);
                invocations.add(invocation);
            }
        }
        return new ThingActionInvocations(context, invocations);
    }

    protected List<ThingAction> confirmActionsAndInvalid() {
        List<ThingAction> actionsToInvoke = new ArrayList<>(actions);
        actions.clear();
        return actionsToInvoke;
    }

    protected List<ThingActionInvoker> getSupportInvokers(ThingAction action) {
        List<ThingActionInvoker> supportInvokers = new ArrayList<>();
        for (ThingActionInvoker invoker : invokers) {
            if (invoker.support(action, context)) {
                supportInvokers.add(invoker);
            }
        }
        return supportInvokers;
    }
}
