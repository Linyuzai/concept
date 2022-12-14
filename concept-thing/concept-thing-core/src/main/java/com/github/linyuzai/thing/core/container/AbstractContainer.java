package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Identify;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.event.ThingAddedEvent;
import com.github.linyuzai.thing.core.event.ThingEvent;
import com.github.linyuzai.thing.core.event.ThingRemovedEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractContainer<T extends Identify<T>> implements Container<T> {

    private ThingContext context;

    private Map<String, T> map;

    @Override
    public T get(String id) {
        return map.get(id);
    }

    @Override
    public List<T> list() {
        return Collections.unmodifiableList(new ArrayList<>(map.values()));
    }

    @Override
    public ThingAction add(T add) {
        return ThingAction.create(() -> {
            doAdd(add);
            return () -> createAddedEvent(add);
        });
    }

    protected void doAdd(T add) {
        map.put(add.getId(), add);
    }

    protected ThingEvent createAddedEvent(T add) {
        return new ThingAddedEvent(context, ThingAction.ADD, this, add);
    }

    @Override
    public ThingAction remove(String id) {
        return ThingAction.create(() -> {
            T remove = doRemove(id);
            return () -> createRemovedEvent(id, remove);
        });
    }

    protected T doRemove(String id) {
        return map.remove(id);
    }

    protected ThingEvent createRemovedEvent(String id, T removed) {
        return new ThingRemovedEvent(context, ThingAction.REMOVE, this, id, removed);
    }
}
