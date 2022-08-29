package com.github.linyuzai.inherit.plugin.intellij;

import java.util.Collection;
import java.util.stream.Collectors;

public enum InheritFlag {

    BUILDER,

    GETTER,

    SETTER;

    public static Collection<String> of(Collection<String> flags) {
        return flags.stream()
                .filter(it -> !it.equals("InheritFlag"))
                .collect(Collectors.toSet());
    }

    public static boolean hasFlag(Collection<String> flags) {
        return !flags.isEmpty();
    }
}
