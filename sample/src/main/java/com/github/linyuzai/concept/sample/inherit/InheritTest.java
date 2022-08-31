package com.github.linyuzai.concept.sample.inherit;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.annotation.InheritField;
import com.github.linyuzai.inherit.core.flag.InheritFlag;

public class InheritTest {
    public static class InheritA {
        private String a;
    }
    public static class InheritB {
        private String b;
    }
    @InheritClass(sources = {InheritA.class, InheritB.class})
    public static class InheritC {
        private String c;

        @InheritField(sources = InheritC.class, flags = InheritFlag.BUILDER)
        public static class Builder {

            public InheritC build() {
                if (a == null) {
                    a = "a";
                }
                return null;
            }
        }
    }

    public static void main(String[] args) {
        new InheritC.Builder()
                .a("a")
                .b("b")
                .c("c")
                .build();
    }
}
