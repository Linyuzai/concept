package com.github.linyuzai.thing.core;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.Map;

public class Test {

    public void test0(Thing thing) {
        thing.getAttributes().get("color").update("RED").chain().invoke().toEvent().publish();
    }

    public void test1(Thing thing, Map<String, Object> attributes) {
        thing.getAttributes().update(attributes).chain().invoke().toEvent().publish();
    }

    public void test2(Thing device) {
    }

    public void test3(Thing thing) {
        Something something = thing.create(Something.class);
        something.setColor("RED");
    }

    public interface Something {

        void setColor(String color);
    }
}
