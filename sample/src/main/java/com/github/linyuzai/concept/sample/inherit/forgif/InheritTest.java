package com.github.linyuzai.concept.sample.inherit.forgif;

import com.github.linyuzai.inherit.core.annotation.InheritClass;

public class InheritTest {

    public static class A {

        private String a;

        private void ma() {}
    }

    @InheritClass(sources = A.class)
    public static class B {

        void mb() {
            a = "a";
            ma();
        }
    }
}
