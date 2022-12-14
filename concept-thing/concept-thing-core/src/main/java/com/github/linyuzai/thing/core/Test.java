package com.github.linyuzai.thing.core;

import com.github.linyuzai.thing.core.action.ThingAction;
import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.Map;
import java.util.function.Function;

public class Test {

    public void test0(Thing thing) {
        thing.getAttributes().get("color").update("RED").execute().toEvent().publish();
    }

    public void test1(Thing thing, Map<String, Object> attributes) {
        thing.getAttributes().update(attributes).execute().toEvent().publish();
    }

    public void test2(Thing thing) {
        thing.getCategories().add("A", new Function<Category, ThingAction>() {
            @Override
            public ThingAction apply(Category category) {
                return category.getCategories().add("B");
            }
        }).execute();
    }

    public void test3(Thing thing) {
        Something something = thing.create(Something.class);
        something.setColor("RED");
    }

    public interface Something {

        void setColor(String color);
    }
}
