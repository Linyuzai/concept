package com.github.linyuzai.thing.core;

import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;
import com.github.linyuzai.thing.core.operation.Operation;

import java.util.Map;

public class Test {

    public void test0(Thing thing) {
        Operation color = thing.attribute("color").update("RED");
        Operation size = thing.attribute("size").update("200");
        color.merge(size).toEvent().publish();
    }

    public void test1(Thing thing, Map<String, Object> attributes) {
        thing.attributes().update(attributes).toEvent().publish();
    }

    public void test2(Thing thing) {
        Something something = thing.create(Something.class);
        Operation color = something.color().update("RED");
        Operation size = something.size().update("200");
        color.merge(size).toEvent().publish();
    }

    public interface Something {

        Attribute color();

        Attribute size();
    }
}
