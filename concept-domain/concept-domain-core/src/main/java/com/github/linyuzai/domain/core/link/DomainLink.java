package com.github.linyuzai.domain.core.link;

import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.DomainRepository;
import com.github.linyuzai.domain.core.exception.DomainRequiredException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DomainLink {

    private static final Map<Object, Map<Object, Object>> CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends DomainObject> Class<? extends DomainRepository<T>> repository(Class<T> domainClass) {
        return (Class<? extends DomainRepository<T>>) CACHE
                .computeIfAbsent(DomainRepositoryLink.class, type -> new ConcurrentHashMap<>())
                .computeIfAbsent(domainClass, key -> {
                    DomainRepositoryLink annotation = domainClass.getAnnotation(DomainRepositoryLink.class);
                    if (annotation == null) {
                        throw new DomainRequiredException("@DomainRepositoryLink");
                    }
                    return annotation.value();
                });
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends DomainObject> owner(Class<? extends DomainObject> domainClass) {
        return (Class<? extends DomainObject>) CACHE
                .computeIfAbsent(DomainOwnerLink.class, type -> new ConcurrentHashMap<>())
                .computeIfAbsent(domainClass, key -> {
                    DomainOwnerLink annotation = domainClass.getAnnotation(DomainOwnerLink.class);
                    if (annotation == null) {
                        throw new DomainRequiredException("@DomainOwnerLink");
                    }
                    return annotation.value();
                });
    }
}
