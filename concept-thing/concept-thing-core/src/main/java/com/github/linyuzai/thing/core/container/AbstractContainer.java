package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ContextThingAction;
import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.IdAndKey;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEvent;

public abstract class AbstractContainer<T extends IdAndKey> implements Container<T> {

    protected abstract ThingContext getContext();

    @Override
    public ThingAction add(T add) {
        return ContextThingAction.of(getContext(), () -> {
            onPrepare(add);
            onValid(add);
            onAdd(add);
            return () -> createAddedEvent(add);
        });
    }

    protected abstract void onPrepare(T add);

    protected abstract void onValid(T add);

    protected abstract void onAdd(T add);

    protected abstract ThingEvent createAddedEvent(T add);

    @Override
    public ThingAction remove(String id) {
        return ContextThingAction.of(getContext(), () -> {
            T remove = onRemove(id);
            return () -> createRemovedEvent(id, remove);
        });
    }

    protected abstract T onRemove(String id);

    protected abstract ThingEvent createRemovedEvent(String id, T removed);
}
