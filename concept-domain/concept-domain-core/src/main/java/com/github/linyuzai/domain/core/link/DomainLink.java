package com.github.linyuzai.domain.core.link;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.exception.DomainException;
import com.github.linyuzai.domain.core.exception.DomainRequiredException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class DomainLink {

    private static Function<Class<? extends DomainObject>, Class<? extends DomainRepository<?, ?>>> repositoryFinder = new Function<Class<? extends DomainObject>, Class<? extends DomainRepository<?, ?>>>() {
        @Override
        public Class<? extends DomainRepository<?, ?>> apply(Class<? extends DomainObject> domainClass) {
            String repository = domainClass.getName() + "Repository";
            try {
                return (Class<? extends DomainRepository<?, ?>>) Class.forName(repository);
            } catch (ClassNotFoundException ignore) {
                return null;
            }
        }
    };

    public static void setRepositoryFinder(Function<Class<? extends DomainObject>, Class<? extends DomainRepository<?, ?>>> repositoryFinder) {
        DomainLink.repositoryFinder = repositoryFinder;
    }

    private static final Map<Object, Map<Object, Object>> CACHE = new ConcurrentHashMap<>();

    public static <T> Class<T> generic(Class<?> target, int index) {
        return (Class<T>) CACHE
                .computeIfAbsent(DomainLink.class, type -> new ConcurrentHashMap<>())
                .computeIfAbsent(target, key -> {
                    Type type = target.getGenericSuperclass();
                    if (type instanceof ParameterizedType) {
                        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                        return types[index];
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
                                if (pt.getRawType() == DomainCollection.class) {
                                    Type[] arguments = pt.getActualTypeArguments();
                                    return arguments[0];
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
                            throw new DomainRequiredException("@DomainRepositoryLink");
                        } else {
                            return repository;
                        }
                    } else {
                        return annotation.value();
                    }
                });
    }
}
