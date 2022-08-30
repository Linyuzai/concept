package com.github.linyuzai.concept.sample.inherit;

import com.github.linyuzai.concept.sample.inherit.builder.BuilderTest2;
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
            flags = {InheritFlag.BUILDER})
    public static class InheritC {

        private String c;

        private void mc() {

        }
    }

    public static void main(String[] args) {
        BuilderTest2 build = new BuilderTest2.Builder()
                .build();
    }
}
