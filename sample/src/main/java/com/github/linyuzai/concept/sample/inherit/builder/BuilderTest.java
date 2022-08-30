package com.github.linyuzai.concept.sample.inherit.builder;

import com.github.linyuzai.inherit.core.annotation.InheritField;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
public class BuilderTest {

    private String string;

    private int i;

    private List<?> list;

    @InheritField(sources = BuilderTest.class)
    public static class Builder {

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
