package com.github.linyuzai.concept.sample.feizhu.reconsitution.context;

public interface OrderContext {

    void put(Object key, Object value);

    <T> T get(Object key);

    void publishEvent(Object event);

    class Key {

        public static final String PAY_ID = "PayId";
    }
}
