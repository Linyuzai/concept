package com.github.linyuzai.plugin.core.matcher;

public abstract class GenericTypePluginMatcher<T> extends AbstractPluginMatcher<T> {

    private final Class<?> matchingClass = getMatchingClass();

    public boolean matchClass(Class<?> clazz, boolean equals) {
        if (equals) {
            return matchingClass == clazz;
        } else {
            return matchingClass.isAssignableFrom(clazz);
        }
    }

    public boolean matchInstance(Object instance) {
        return matchingClass.isInstance(instance);
    }

    public abstract Class<?> getMatchingClass();
}
