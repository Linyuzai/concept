package com.github.linyuzai.domain.mbp;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.linyuzai.domain.core.DomainIdGenerator;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于 MyBatis-Plus 的 id 生成器
 */
public class MBPDomainIdGenerator<T> implements DomainIdGenerator<T> {

    private static final Map<Class<?>, DomainIdGenerator<?>> CACHE = new ConcurrentHashMap<>();

    @Override
    public String generateId(T object) {
        return IdWorker.getIdStr();
    }

    @SuppressWarnings("unchecked")
    public static <G extends DomainIdGenerator<?>> G create(Class<G> clazz) {
        return (G) CACHE.computeIfAbsent(clazz, fun -> (G) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "generateId":
                            return IdWorker.getIdStr();
                        case "toString":
                            return "DomainIdGenerator@" + clazz.getName();
                        case "equals":
                            return args.length == 1 && args[0] == proxy;
                        case "hashCode":
                            return clazz.hashCode();
                        default:
                            return method.invoke(proxy, args);
                    }
                }));
    }
}
