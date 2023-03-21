package com.github.linyuzai.thing.core.concept;

public interface ThingConcept {

    Category createCategory(String key, String name);

    Thing createThing(String key, String name);
}
