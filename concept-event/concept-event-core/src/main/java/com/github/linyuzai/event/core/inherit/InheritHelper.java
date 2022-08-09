package com.github.linyuzai.event.core.inherit;

import com.github.linyuzai.event.core.config.PropertiesConfig;
import com.github.linyuzai.event.core.exception.EventException;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class InheritHelper {

    protected final Pattern pattern = Pattern.compile("[A-Z]");

    public void inherit(Inheritable inheritable, Class<?> clazz, String prefix) {
        Map<String, ? extends Inheritable.Endpoint> endpoints = inheritable.getEndpoints();
        for (Map.Entry<String, ? extends Inheritable.Endpoint> entry : endpoints.entrySet()) {
            String name = entry.getKey();
            Inheritable.Endpoint ie = entry.getValue();
            String inheritName = ie.getInherit();
            if (inheritName != null && !inheritName.isEmpty()) {
                Inheritable.Endpoint inherit = endpoints.get(inheritName);
                if (inherit == null) {
                    throw new EventException("Inherit endpoint not found: " + inheritName);
                }
                inherit(ie, inherit, clazz, prefix + "." + formatKey(name));
            }
        }
    }

    @SneakyThrows
    public void inherit(Object child, Object parent, Class<?> clazz, String prefix) {
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getName().equals("this$0")) {
                continue;
            }
            if (needInherit(field)) {
                doInherit(field, child, parent, prefix);
            }
        }
    }

    public abstract boolean needInherit(Field field);

    public void doInherit(Field field, Object child, Object parent, String prefix) {
        Class<?> type = field.getType();
        if (field.getType() == Map.class) {
            inheritMap(getValue(field, child), getValue(field, parent));
        } else if (type == String.class ||
                type == Class.class ||
                type.isPrimitive() ||
                isWrapClass(type) ||
                type.isEnum() ||
                isValueType(type)) {
            if (hasCustomValue(prefix + "." + formatKey(field.getName()))) {
                return;
            }
            setValue(field, child, getValue(field, parent));
        } else {
            inherit(getValue(field, child), getValue(field, parent), type,
                    prefix + "." + formatKey(field.getName()));
        }
    }

    public abstract boolean hasCustomValue(String key);

    public boolean isValueType(Class<?> clazz) {
        return false;
    }

    public String formatKey(String key) {
        Matcher matcher = pattern.matcher(key);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "-" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Deprecated
    private String x(String str) {
        str = str.toLowerCase();
        Pattern compile = Pattern.compile("_[a-z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(0).toUpperCase().replace("_", ""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Field field, Object object) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return (T) field.get(object);
    }

    @SneakyThrows
    public static void setValue(Field field, Object object, Object value) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(object, value);
    }

    public static boolean isWrapClass(Class<?> clazz) {
        try {
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    public static <K, V> void inheritMap(Map<K, V> child, Map<K, V> parent) {
        for (Map.Entry<K, V> entry : parent.entrySet()) {
            K key = entry.getKey();
            if (child.containsKey(key)) {
                continue;
            }
            child.put(key, entry.getValue());
        }
    }

    public static void inherit(PropertiesConfig child, PropertiesConfig parent) {
        inheritMap(child.getMetadata(), parent.getMetadata());
        if (child.getEncoder() == null) {
            child.setEncoder(parent.getEncoder());
        }
        if (child.getDecoder() == null) {
            child.setDecoder(parent.getDecoder());
        }
        if (child.getErrorHandler() == null) {
            child.setErrorHandler(parent.getErrorHandler());
        }
        if (child.getPublisher() == null) {
            child.setPublisher(parent.getPublisher());
        }
        if (child.getSubscriber() == null) {
            child.setSubscriber(parent.getSubscriber());
        }
    }
}
