package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.InnerThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.Attribute;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
public class AttributesImpl extends AbstractAttributes {

    private final Map<String, Attribute> attributes;

    @Override
    public Attribute get(String id) {
        return attributes.get(id);
    }

    @Override
    public Optional<Attribute> optional(String id) {
        return Optional.ofNullable(get(id));
    }

    @Override
    public List<Attribute> list() {
        return Collections.unmodifiableList(new ArrayList<>(attributes.values()));
    }

    @Override
    public Stream<Attribute> stream() {
        return list().stream();
    }

    @Override
    public ThingActionChain add(Attribute one) {
        return getThing().actions().next(new InnerThingAction(() -> {
            attributes.put(one.getId(), one);
            return new AddInvocation(one);
        }));
    }

    @Override
    public ThingActionChain remove(String id) {
        return getThing().actions().next(new InnerThingAction(() -> {
            Attribute remove = attributes.remove(id);
            return null;
        }));
    }
}
