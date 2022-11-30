package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Relationships;
import com.github.linyuzai.thing.core.container.Things;
import com.github.linyuzai.thing.core.context.ThingContext;

public interface ThingFactory {

    Thing create(String name, Categories categories, Attributes attributes, Relationships relationships, ThingContext context);

    Things createContainer();
}
