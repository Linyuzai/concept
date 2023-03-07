package com.github.linyuzai.concept.sample.inherit.bug6;

import com.github.linyuzai.concept.sample.inherit.bug6.d.Bug6D;
import com.github.linyuzai.inherit.core.annotation.InheritClass;

@InheritClass(sources = Bug6D.class)
public class Bug6Sample {

    public void sample() {

    }

    public static class X implements Runnable {

        @Override
        public void run() {

        }
    }

    @InheritClass(sources = X.class)
    public static class Y {

    }
}
