package com.github.linyuzai.concept.sample.inherit.builder;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.annotation.InheritField;
import com.github.linyuzai.inherit.core.flag.InheritFlag;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class BuilderTest2 extends BuilderTest {

    private Map<String, String> map;

    public BuilderTest2(String string, int i, List<?> list, Map<String, String> map) {
        super(string, i, list);
        this.map = map;
    }

    @InheritClass(sources = BuilderTest2.class, inheritSuper = true, flags = InheritFlag.BUILDER)
    public static class Builder extends BuilderTest.Builder {

        @Override
        public BuilderTest2 build() {
            valid();
            if (map == null) {
                map = new HashMap<>();
            }
            return new BuilderTest2(string, i, list, map);
        }
    }
}
