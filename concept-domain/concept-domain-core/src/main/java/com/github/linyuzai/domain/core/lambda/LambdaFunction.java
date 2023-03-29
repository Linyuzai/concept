package com.github.linyuzai.domain.core.lambda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 接收 lambda 表达式
 */
@FunctionalInterface
public interface LambdaFunction<T, R> extends Function<T,R>, Serializable {

    Map<Class<?>, SerializedLambda> CACHE = new ConcurrentHashMap<>();

    default SerializedLambda getSerializedLambda() {
        return CACHE.computeIfAbsent(this.getClass(), new MappingFunction(this));
    }

    static Name getClassName(SerializedLambda sl) {
        return new Name(sl.getInstantiatedMethodType())
                .convertMethodTypeToClass()
                .toSimple();
    }

    static Name getMethodName(SerializedLambda sl) {
        return new Name(sl.getImplMethodName())
                .convertGetMethodToField();
    }

    @Getter
    @AllArgsConstructor
    class Name {

        private String value;

        /**
         * get 方法转字段名
         */
        public Name convertGetMethodToField() {
            if (value.startsWith("get") && !value.equals("get")) {
                value = value.substring(3);
            } else if (value.startsWith("is") && !value.equals("is")) {
                value = value.substring(2);
            }
            return this;
        }

        /**
         * 获得类名
         */
        public Name convertMethodTypeToClass() {
            if (value.startsWith("(L")) {
                value = value.substring(2).split(";\\)")[0].replaceAll("/", ".");
            }
            return this;
        }

        /**
         * 简化类名
         */
        public Name toSimple() {
            int index = value.lastIndexOf(".");
            if (index >= 0) {
                value = value.substring(index + 1);
            }
            return this;
        }

        /**
         * 第一个字母转小写
         */
        public Name lowercaseFirst() {
            value = Character.toLowerCase(value.charAt(0)) + value.substring(1);
            return this;
        }
    }

    @AllArgsConstructor
    class MappingFunction implements Function<Class<?>, SerializedLambda> {

        private final Object target;

        @SneakyThrows
        @Override
        public SerializedLambda apply(Class<?> clazz) {
            Method method = clazz.getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(target);
        }
    }
}
