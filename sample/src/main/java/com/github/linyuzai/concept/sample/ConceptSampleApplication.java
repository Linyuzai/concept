package com.github.linyuzai.concept.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class ConceptSampleApplication {

    public static void main(String[] args) {
        //SpringApplication.run(ConceptSampleApplication.class, args);
        for (Type type : B.class.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                System.out.println(Arrays.toString(types));
            }
        }
        TypeVariable<Class<A[]>>[] variables = A[].class.getTypeParameters();
        System.out.println(Arrays.toString(variables));
    }

    public interface A<T> {

        void a(T t);
    }

    public static class B<T extends String> implements A<T> {

        @Override
        public void a(T t) {

        }
    }
}
