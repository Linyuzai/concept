package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ContextThingAction;
import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Identify;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractContainer<T extends Identify> implements Container<T> {

    protected abstract ThingContext getContext();

    protected abstract Map<String, T> getMap();

    @Override
    public T get(String id) {
        return getMap().get(id);
    }

    @Override
    public List<T> list() {
        return Collections.unmodifiableList(new ArrayList<>(getMap().values()));
    }

    @Override
    public ThingAction add(T add) {
        return ContextThingAction.of(getContext(), () -> {
            doAdd(add);
            return () -> createAddedEvent(add);
        });
    }

    protected void doAdd(T add) {
        getMap().put(add.getId(), add);
    }

    protected abstract ThingEvent createAddedEvent(T add);

    @Override
    public ThingAction remove(String id) {
        return ContextThingAction.of(getContext(), () -> {
            T remove = doRemove(id);
            return () -> createRemovedEvent(id, remove);
        });
    }

    protected T doRemove(String id) {
        return getMap().remove(id);
    }

    protected abstract ThingEvent createRemovedEvent(String id, T removed);
}
