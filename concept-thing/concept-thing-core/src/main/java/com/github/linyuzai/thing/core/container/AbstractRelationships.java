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
        return add(relation, name, null, null);
    }

    @Override
    public ThingAction add(Thing relation, String name, Function<Relationship, ThingAction> next) {
        return add(relation, name, null, next);
    }

    @Override
    public ThingAction add(Thing relation, String name, String opposite) {
        return add(relation, name, opposite, null);
    }

    @Override
    public ThingAction add(Thing relation, String name, String opposite, Function<Relationship, ThingAction> next) {
        Relationship relationship = create(relation, name, opposite);
        ThingAction action = add(relationship);
        if (next == null) {
            return action;
        } else {
            ThingAction apply = next.apply(relationship);
            ThingActionChain chain = getContext().actions();
            return chain.next(action).next(apply);
        }
    }

    protected Relationship create(Thing relation, String name, String opposite) {
        RelationshipFactory factory = getContext().get(RelationshipFactory.class);
        Relationship relationship = factory.create();
        relationship.setName(name);
        relationship.setThing(thing);
        relationship.setRelation(relation);
        relationship.setOpposite(relationship.getOpposite());
        relationship.setContext(getContext());
        if (opposite != null) {
            relationship.getOpposite().setName(opposite);
        }
        return relationship;
    }
}
