package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.action.ThingActionChain;
import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Relationships;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.factory.AttributeFactory;
import com.github.linyuzai.thing.core.factory.CategoryFactory;
import com.github.linyuzai.thing.core.factory.RelationshipFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractThing extends AbstractIdentify<Thing> implements Thing {

    private String name;

    private Categories categories;

    private Attributes attributes;

    private Relationships relationships;

    private ThingContext context;

    @Override
    public Categories getCategories() {
        if (categories == null) {
            CategoryFactory factory = context.get(CategoryFactory.class);
            categories = factory.createContainer();
            categories.setContext(context);
        }
        return categories;
    }

    @Override
    public Attributes getAttributes() {
        if (attributes == null) {
            AttributeFactory factory = context.get(AttributeFactory.class);
            attributes = factory.createContainer();
            attributes.setThing(this);
            attributes.setContext(context);
        }
        return attributes;
    }

    @Override
    public Relationships getRelationships() {
        if (relationships == null) {
            RelationshipFactory factory = context.get(RelationshipFactory.class);
            relationships = factory.createContainer();
            relationships.setThing(this);
        }
        return relationships;
    }

    @Override
    public ThingActionChain actions() {
        return context.actions();
    }

    @Override
    public void publish(Object event) {
        context.publish(event);
    }

    @Override
    public <T> T create(Class<T> target) {
        return null;
    }
}
