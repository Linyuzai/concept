package com.github.linyuzai.attribute.dynamic.core.concept;

import java.util.*;

public class AbstractAttributeConcept implements AttributeConcept {

    private final List<Attribute> attributes = new ArrayList<>();

    public Attribute getAttribute(String id) {
        return getAttribute(id, attributes);
    }

    public <T> T getValue(String id) {
        Attribute attribute = getAttribute(id);
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    public static Attribute getAttribute(String id, Collection<Attribute> attributes) {
        return getAttribute(id, attributes.toArray(new Attribute[0]));
    }

    public static Attribute getAttribute(String id, Attribute... attributes) {
        for (Attribute attribute : attributes) {
            if (attribute.getId().equals(id)) {
                return attribute;
            }
            Object value = attribute.getValue();
            if (value instanceof Attribute) {
                Attribute attr = getAttribute(id, (Attribute) value);
                if (attr != null) {
                    return attr;
                }
            }
            if (value instanceof Collection) {
                for (Object v : (Collection<?>) value) {
                    if (v instanceof Attribute) {
                        Attribute attr = getAttribute(id, (Attribute) v);
                        if (attr != null) {
                            return attr;
                        }
                    }
                }
            }
        }
        return null;
    }
}
