package com.github.linyuzai.concept.sample.inherit;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.flag.InheritFlag;

public class InheritTest {

    public static class InheritA {

        private String a;

        private void ma() {

        }
    }

    public static class InheritB {

        private String b;

        private void mb() {

        }
    }

    @InheritClass(sources = {InheritA.class, InheritB.class},
            flags = {InheritFlag.BUILDER, InheritFlag.GETTER, InheritFlag.SETTER})
    public static class InheritC {

        private String c;

        private void mc() {

        }
    }
}
