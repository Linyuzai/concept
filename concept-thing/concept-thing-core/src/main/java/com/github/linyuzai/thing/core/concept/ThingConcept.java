package com.github.linyuzai.thing.core.concept;

public interface ThingConcept {

    Thing createThing(String name);

    Category createCategory(String name, Category category);

    Label createLabel(String name, Category category);

    Attribute createAttribute(Label label, Thing thing);

    Relationship createRelationship(String name, Thing thing, Thing relation);
}
