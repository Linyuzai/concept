package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.concept.Relationship;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.factory.RelationshipFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public abstract class AbstractRelationships extends AbstractContainer<Relationship> implements Relationships {

    private Thing thing;

    @Override
    public ThingAction add(Thing relation, String name) {
        return add(create(relation, name));
    }

    @Override
    public ThingAction add(Thing relation, String name, Function<Relationship, ThingAction> next) {
        Relationship relationship = create(relation, name);
        ThingAction action = add(relationship);
        ThingAction apply = next.apply(relationship);
        ThingActionChain chain = getContext().actions();
        return chain.next(action).next(apply);
    }

    @Override
    public ThingAction add(Thing relation, String name, String opposite) {
        Relationship relationship = create(relation, name);
        relationship.getOpposite().setName(opposite);
        return add(relationship);
    }

    @Override
    public ThingAction add(Thing relation, String name, String opposite, Function<Relationship, ThingAction> next) {
        Relationship relationship = create(relation, name);
        relationship.getOpposite().setName(opposite);
        ThingAction action = add(relationship);
        ThingAction apply = next.apply(relationship);
        ThingActionChain chain = getContext().actions();
        return chain.next(action).next(apply);
    }

    protected Relationship create(Thing relation, String name) {
        RelationshipFactory factory = getContext().get(RelationshipFactory.class);
        Relationship relationship = factory.create();
        relationship.setName(name);
        relationship.setThing(thing);
        relationship.setRelation(relation);
        Relationship opposite = relationship.getOpposite();
        relationship.setOpposite(opposite);
        relationship.setContext(getContext());
        return relationship;
    }
}
