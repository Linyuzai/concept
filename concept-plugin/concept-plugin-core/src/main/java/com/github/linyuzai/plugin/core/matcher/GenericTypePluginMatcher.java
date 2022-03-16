package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;

@SuppressWarnings("unchecked")
public abstract class GenericTypePluginMatcher<T> extends AbstractPluginMatcher<T> {

    private final Type matchingType = getMatchingType();

    @Override
    public boolean tryMatch(PluginContext context) {
        return tryMatch(context, matchingType, new Annotation[0]);
    }

    public abstract boolean tryMatch(PluginContext context, Type type, Annotation[] annotations);

    public Type getMatchingType() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types.length == 1) {
                return types[0];
            }
        }
        throw new PluginException("U may need to try override this method");
    }

    @SneakyThrows
    public <E> List<E> newList(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new ArrayList<>();
        } else {
            return (List<E>) clazz.newInstance();
        }
    }

    @SneakyThrows
    public <E> Set<E> newSet(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new HashSet<>();
        } else {
            return (Set<E>) clazz.newInstance();
        }
    }

    @SneakyThrows
    public <E> Map<String, E> newMap(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)) {
            return new HashMap<>();
        } else {
            return (Map<String, E>) clazz.newInstance();
        }
    }

    public Class<?> toClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return (Class<?>) rawType;
            }
            return null;
        } else if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length > 0) {
                return toClass(upperBounds[0]);
            }
            return null;
        }
        return null;
    }

    public Metadata createMetadata(Class<?> clazz, Type target) {
        Metadata metadata = new Metadata();
        if (Map.class.isAssignableFrom(clazz)) {
            metadata.map = newMap(clazz);
            metadata.target = target;
        } else if (List.class.isAssignableFrom(clazz)) {
            metadata.list = newList(clazz);
            metadata.target = target;
        } else if (Set.class.isAssignableFrom(clazz)) {
            metadata.set = newSet(clazz);
            metadata.target = target;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            metadata.collection = newList(clazz);
            metadata.target = target;
        } else {
            metadata.target = clazz;
        }
        return metadata;
    }

    public Metadata getMetadata(Type type) {
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            return createMetadata(clazz, Object.class);
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (rawType instanceof Class) {
                Class<?> clazz = (Class<?>) rawType;
                Metadata metadata = createMetadata(clazz, actualTypeArguments[0]);
                if (metadata.isMap()) {
                    Type actualTypeArgument0 = actualTypeArguments[0];
                    if (actualTypeArgument0 instanceof Class &&
                            ((Class<?>) actualTypeArgument0).isAssignableFrom(String.class)) {
                        metadata.target = actualTypeArguments[1];
                    } else {
                        throw new PluginException("Map key must be String");
                    }
                }
                return metadata;
            }
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length > 0) {
                return getMetadata(upperBounds[0]);
            }
            //TODO ? super xxx 好像没有必要
        }
        return null;
    }

    public boolean setMatchedValue(PluginContext context, Metadata metadata, Map<String, ?> map, String typeMsg) {
        if (map.isEmpty()) {
            return false;
        }
        if (metadata.isMap()) {
            metadata.getMap().putAll(map);
            context.set(this, metadata.getMap());
            return true;
        } else if (metadata.isList()) {
            metadata.getList().addAll(map.values());
            context.set(this, metadata.getList());
            return true;
        } else if (metadata.isSet()) {
            metadata.getSet().addAll(map.values());
            context.set(this, metadata.getSet());
            return true;
        } else if (metadata.isCollection()) {
            metadata.getCollection().addAll(map.values());
            context.set(this, metadata.getCollection());
            return true;
        } else {
            List<?> list = new ArrayList<>(map.values());
            if (list.size() > 1) {
                throw new PluginException("More than one " + typeMsg + " matched: " + list);
            }
            context.set(this, list.get(0));
            return true;
        }
    }

    @Data
    public static class Metadata {

        private Map<String, Object> map;

        private List<Object> list;

        private Set<Object> set;

        private Collection<Object> collection;

        private Type target;

        public boolean isMap() {
            return map != null;
        }

        public boolean isList() {
            return list != null;
        }

        public boolean isSet() {
            return set != null;
        }

        public boolean isCollection() {
            return collection != null;
        }
    }
}
