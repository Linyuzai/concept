package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Relationship;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.container.Relationships;

public interface RelationshipFactory {

    Relationship create(String name, Thing relation);

    Relationships createContainer();
}
