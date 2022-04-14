package com.github.linyuzai.attribute.dynamic.core.concept;

import com.github.linyuzai.attribute.dynamic.core.utils.MessageFormatter;

import java.util.*;

public class AbstractAttributeConcept implements AttributeConcept {

    private final List<Attribute> attributes = new ArrayList<>();

    public Attribute getAttribute(String id) {
        for (Attribute attribute : attributes) {
            if (attribute.getId().equals(id)) {
                return attribute;
            }
        }
        return null;
    }

    public <T> T getValue(String id) {
        Attribute attribute = getAttribute(id);
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    public Map<String, Object> getValues() {
        Map<String, Object> values = new LinkedHashMap<>();
        for (Attribute attribute : attributes) {
            values.put(attribute.getId(), attribute.getValue());
        }
        return values;
    }

    public boolean update(String id, Object value) {
        Attribute attribute = getAttribute(id);
        if (attribute == null) {
            return false;
        }
        return attribute.update(value);
    }

    public Map<String, Object> update(Map<String, Object> values) {
        Map<String, Object> updatedValues = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String id = entry.getKey();
            Attribute attribute = getAttribute(id);
            if (attribute == null) {
                continue;
            }
            if (attribute.update(entry.getValue())) {
                updatedValues.put(id, attribute.getValue());
            }
        }
        return updatedValues;
    }

    public Attribute newAttribute(String id, Object... args) {
        Attribute attribute = getAttribute(id);
        if (attribute == null) {
            return null;
        }
        String formattedId;
        if (args.length == 0) {
            formattedId = id;
        } else {
            formattedId = MessageFormatter.arrayFormat(id, args).getMessage();
        }
        Attribute newAttribute = attribute.getFactory().create(formattedId);
        attributes.add(newAttribute);
        return newAttribute;
    }
}
