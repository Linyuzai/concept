package com.github.linyuzai.extension.core.concept.simple;

import com.github.linyuzai.extension.core.concept.Extension;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleResult implements Extension.Result {

    private final String code;

    private final String message;

    private final Object object;

    public static class Builder {

        private String code;

        private String message;

        private Object object;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder object(Object object) {
            this.object = object;
            return this;
        }

        public SimpleResult build() {
            return new SimpleResult(code, message, object);
        }
    }
}
