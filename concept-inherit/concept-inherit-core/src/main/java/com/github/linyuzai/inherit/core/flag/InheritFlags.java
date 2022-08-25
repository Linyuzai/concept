package com.github.linyuzai.inherit.core.flag;

import java.util.Collection;
import java.util.HashSet;

public class InheritFlags {

    public static final String GENERATE_METHODS_WITH_FIELDS = "GENERATE_METHODS_WITH_FIELDS";

    public static final String GETTER_TEMPLATE = "GETTER_TEMPLATE";

    public static final String SETTER_TEMPLATE = "SETTER_TEMPLATE";

    public static final String BUILDER_TEMPLATE = "BUILDER_TEMPLATE";

    public static final String GETTER = GENERATE_METHODS_WITH_FIELDS + "," + GETTER_TEMPLATE;

    public static final String SETTER = GENERATE_METHODS_WITH_FIELDS + "," + SETTER_TEMPLATE;

    public static final String BUILDER = GENERATE_METHODS_WITH_FIELDS + "," + BUILDER_TEMPLATE;

    public static Collection<String> of(Collection<String> flags) {
        Collection<String> set = new HashSet<>();
        for (String flag : flags) {
            for (String s : flag.split(",")) {
                set.add(s.trim());
            }
        }
        return set;
    }
}
