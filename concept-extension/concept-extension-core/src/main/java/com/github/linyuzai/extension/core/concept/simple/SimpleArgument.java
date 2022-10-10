package com.github.linyuzai.extension.core.concept.simple;

import com.github.linyuzai.extension.core.concept.Extension;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleArgument implements Extension.Argument {

    private final Object target;

    private final String key;

    private final Object value;

    private final Map<Object, Object> configs;

    public static class Builder {

        private Object target;

        private String key;

        private Object value;

        private Map<Object, Object> configs;

        public Builder target(Object target) {
            this.target = target;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public Builder config(Object key, Object value) {
            getOrCreateConfigs().put(key, value);
            return this;
        }

        public Builder configs(Map<Object, Object> configs) {
            getOrCreateConfigs().putAll(configs);
            return this;
        }

        private Map<Object, Object> getOrCreateConfigs() {
            if (configs == null) {
                configs = new LinkedHashMap<>();
            }
            return configs;
        }

        public SimpleArgument build() {
            return new SimpleArgument(target, key, value, configs);
        }
    }
}
