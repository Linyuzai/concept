package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Attributes;
import com.github.linyuzai.thing.core.container.Relationships;
import com.github.linyuzai.thing.core.context.ThingContext;
import com.github.linyuzai.thing.core.factory.AttributeFactory;
import com.github.linyuzai.thing.core.factory.RelationshipFactory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractThing extends AbstractIdentify implements Thing {

    private String name;

    private Category category;

    private Attributes attributes;

    private Relationships relationships;

    private ThingContext context;

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
    public void publish(Object event) {
        context.publish(event);
    }

    @Override
    public <T> T create(Class<T> target) {
        return null;
    }
}
