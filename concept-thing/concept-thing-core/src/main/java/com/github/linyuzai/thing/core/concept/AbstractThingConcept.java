package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Relationships;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.context.ThingContextFactory;
import com.github.linyuzai.thing.core.factory.AttributeFactory;
import com.github.linyuzai.thing.core.factory.CategoryFactory;
import com.github.linyuzai.thing.core.factory.RelationshipFactory;
import com.github.linyuzai.thing.core.factory.ThingFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public abstract class AbstractThingConcept implements ThingConcept {

    private ThingContextFactory contextFactory;

    private ThingFactory thingFactory;

    private CategoryFactory categoryFactory;

    private AttributeFactory attributeFactory;

    private RelationshipFactory relationshipFactory;

    @Override
    public Thing createThing(String name) {
        ThingContext context = contextFactory.create();

        Categories categories = categoryFactory.createContainer();

        Collection<Consumer<Thing>> attributesConsumer = new ArrayList<>();
        Attributes attributes = attributeFactory.createContainer(attributesConsumer);

        Relationships relationships = relationshipFactory.createContainer();

        Thing thing = thingFactory.create(name, categories, attributes, relationships, context);

        attributesConsumer.forEach(it -> it.accept(thing));

        return thing;
    }

    @Override
    public Category createCategory(String name, Category category) {

        return null;
    }

    @Override
    public Label createLabel(String name, Category category) {
        return null;
    }

    @Override
    public Attribute createAttribute(Label label, Thing thing) {
        return null;
    }

    @Override
    public Relationship createRelationship(String name, Thing thing, Thing relation) {
        return null;
    }
}
