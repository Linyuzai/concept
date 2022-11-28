package com.github.linyuzai.thing.core;

import com.github.linyuzai.thing.core.action.state.UpdateStateAction;
import com.github.linyuzai.thing.core.concept.Attribute;
import com.github.linyuzai.thing.core.concept.Thing;

import java.util.Map;

public class Test {

    public void test0(Thing thing) {
    }

    public void test1(Thing thing, Map<String, Object> attributes) {
        thing.getAttributes().update(attributes);
    }

    public void test2(Thing thing) {
        thing.action(new UpdateStateAction("color","RED"))
                .invoke();
    }

    public void test3(Thing thing) {
        Something something = thing.create(Something.class);
        something.color("RED");
    }

    public interface Something {

        Attribute color();

        void color(String color);

        Attribute size();
    }
}
