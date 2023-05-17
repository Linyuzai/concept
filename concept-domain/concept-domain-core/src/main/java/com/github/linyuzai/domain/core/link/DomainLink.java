package com.github.linyuzai.domain.core.link;

import com.github.linyuzai.domain.core.*;
import com.github.linyuzai.domain.core.exception.DomainException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class DomainLink {

    private static Function<Class<? extends DomainObject>, Class<? extends DomainRepository<?, ?>>> repositoryFinder = domainClass ->
            (Class<? extends DomainRepository<?, ?>>) getDomainRepositoryClass(domainClass);

    private static Class<?> getDomainRepositoryClass(Class<?> domainClass) {
        String repository = domainClass.getName() + "Repository";
        try {
            return Class.forName(repository);
        } catch (ClassNotFoundException ignore) {
            Class<?>[] interfaces = domainClass.getInterfaces();
            for (Class<?> domainInterface : interfaces) {
                if (DomainObject.class.isAssignableFrom(domainInterface)
                        && domainInterface != DomainEntity.class
                        && domainInterface != DomainValue.class
                        && domainInterface != DomainObject.class) {
                    Class<?> repositoryClass = getDomainRepositoryClass(domainInterface);
                    if (repositoryClass != null) {
                        return repositoryClass;
                    }
                }
            }
            return null;
        }
    }

    public static void setRepositoryFinder(Function<Class<? extends DomainObject>, Class<? extends DomainRepository<?, ?>>> repositoryFinder) {
        DomainLink.repositoryFinder = repositoryFinder;
    }

    private static final Map<Object, Map<Object, Object>> CACHE = new ConcurrentHashMap<>();

    public static <T> Class<T> generic(Class<?> target, int index) {
        return ((Map<Integer, Class<T>>) CACHE
                .computeIfAbsent(Class.class, cls -> new ConcurrentHashMap<>())
                .computeIfAbsent(target, type -> new ConcurrentHashMap<>()))
                .computeIfAbsent(index, i -> {
                    Type type = target.getGenericSuperclass();
                    if (type instanceof ParameterizedType) {
                        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                        Type t = types[index];
                        Class<T> classForType = getClassForType(t);
                        if (classForType != null) {
                            return classForType;
                        }
                    }
                    throw new DomainException("Can not get generic type");
                });
    }

    public static <T extends DomainObject> Class<T> collection(Class<? extends DomainCollection<?>> target) {
        return (Class<T>) CACHE
                .computeIfAbsent(DomainCollection.class, type -> new ConcurrentHashMap<>())
                .computeIfAbsent(target, key -> {
                    if (target.isInterface()) {
                        for (Type type : target.getGenericInterfaces()) {
                            if (type instanceof ParameterizedType) {
                                ParameterizedType pt = (ParameterizedType) type;
                                Class<?> classForType = getClassForType(pt.getRawType());
                                if (classForType != null && DomainCollection.class.isAssignableFrom(classForType)) {
                                    Type[] arguments = pt.getActualTypeArguments();
                                    return getClassForType(arguments[0]);
                                }
                            }
                        }
                    }
                    throw new DomainException("Can not get generic type");
                });
    }

    public static <R extends DomainRepository<?, ?>> Class<R> repository(Class<? extends DomainObject> domainClass) {
        return (Class<R>) CACHE
                .computeIfAbsent(DomainRepositoryLink.class, type -> new ConcurrentHashMap<>())
                .computeIfAbsent(domainClass, key -> {
                    DomainRepositoryLink annotation = domainClass.getAnnotation(DomainRepositoryLink.class);
                    if (annotation == null) {
                        Class<? extends DomainRepository<?, ?>> repository = repositoryFinder.apply(domainClass);
                        if (repository == null) {
                            throw new DomainException("Add @DomainRepositoryLink on " + domainClass + " or custom DomainLink.setRepositoryFinder");
                        } else {
                            return repository;
                        }
                    } else {
                        return annotation.value();
                    }
                });
    }

    public static <T> Class<T> getClassForType(Type type) {
        if (type instanceof Class) {
            return (Class<T>) type;
        }
        if (type instanceof ParameterizedType) {
            return getClassForType(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            Type[] bounds = ((TypeVariable<?>) type).getBounds();
            if (bounds.length > 0) {
                Type bound = bounds[0];
                return getClassForType(bound);
            }
        }
        return null;
    }
}
