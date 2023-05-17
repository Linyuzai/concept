package com.github.linyuzai.concept.sample.domain;

public interface Users2 extends Users<User> {

    default void test2() {
        System.out.println("test2");
        System.out.println(this);
        System.out.println(getContext());
        System.out.println(getConditions());
        System.out.println(getRepository());
        System.out.println(getExtra());
    }
}
