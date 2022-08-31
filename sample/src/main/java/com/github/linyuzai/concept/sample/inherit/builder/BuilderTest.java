package com.github.linyuzai.concept.sample.inherit.builder;

import com.github.linyuzai.inherit.core.annotation.InheritField;
import com.github.linyuzai.inherit.core.flag.InheritFlag;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
public class BuilderTest {

    protected String string;

    protected int i;

    protected List<?> list;

    @InheritField(sources = BuilderTest.class, flags = {InheritFlag.BUILDER, InheritFlag.OWN})
    public static class Builder {

        private String b1;

        protected void valid() {
            if (string == null) {
                string = "BuilderTest";
            }
            if (list == null) {
                list = new ArrayList<>();
            }
        }

        public BuilderTest build() {
            valid();
            return new BuilderTest(string, i, list);
        }
    }
}
