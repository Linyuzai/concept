package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Relationship;
import com.github.linyuzai.thing.core.concept.RelationshipImpl;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.container.Relationships;
import com.github.linyuzai.thing.core.container.RelationshipsImpl;

public class RelationshipFactoryImpl implements RelationshipFactory {

    @Override
    public Relationship create(String name, Thing relation) {
        return new RelationshipImpl();
    }

    @Override
    public Relationships createContainer() {
        return new RelationshipsImpl();
    }
}
