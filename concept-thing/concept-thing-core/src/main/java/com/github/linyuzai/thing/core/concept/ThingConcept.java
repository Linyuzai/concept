package com.github.linyuzai.thing.core.concept;

public interface ThingConcept {

    Category createCategory(String id, String name);

    Thing createThing(String id, String name);
}
