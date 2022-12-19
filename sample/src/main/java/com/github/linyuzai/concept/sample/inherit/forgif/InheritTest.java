package com.github.linyuzai.concept.sample.inherit.forgif;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.flag.InheritFlag;

public class InheritTest {
    public static class A1 {
        private String a1;

        private void ma1() {
        }
    }

    public static class A2 {
        private String a2;

        private void ma2() {
        }
    }

    @InheritClass(sources = {A1.class, A2.class})
    public static class B {
        private String b;

        void mb() {
        }
    }

    public static void main(String[] args) {
        B b = new B();

    }
}
