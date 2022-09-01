package com.github.linyuzai.concept.sample.inherit;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.annotation.InheritField;
import com.github.linyuzai.inherit.core.flag.InheritFlag;

public class InheritTest {
    public static class Parent {
        protected String p;
    }

    public static class InheritA {
        private String a;
    }

    public static class InheritB {
        private String b;
    }

    @InheritClass(sources = {InheritA.class, InheritB.class})
    public static class InheritC {
        private String c;

        private String p;

        @InheritField(sources = InheritC.class, flags = InheritFlag.BUILDER)
        public static class Builder extends Parent {

            public InheritC build() {
                if (a == null) {
                    a = "a";
                }
                InheritC inheritC = new InheritC();
                inheritC.a = a;
                inheritC.b = b;
                inheritC.c = c;
                inheritC.p = p;
                return inheritC;
            }
        }
    }

    public static void main(String[] args) {
        InheritC build = new InheritC.Builder()
                .b("b")
                .c("c")
                .p("p")
                .build();
        System.out.println(build.a);
        System.out.println(build.b);
        System.out.println(build.c);
        System.out.println(build.p);
    }
}
