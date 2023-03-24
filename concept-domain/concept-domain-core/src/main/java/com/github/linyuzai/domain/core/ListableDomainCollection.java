package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.proxy.DomainCollectionProxy;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class ListableDomainCollection<T extends DomainObject> implements DomainCollection<T>, DomainCollectionProxy {

    @NonNull
    private final List<T> list;

    private Map<String, T> map;

    @Override
    public T get(String id) {
        return getMap().get(id);
    }

    @Override
    public List<T> list() {
        return list;
    }

    @Override
    public Stream<T> stream() {
        return list().stream();
    }

    @Override
    public Long count() {
        return Integer.valueOf(list.size()).longValue();
    }

    public Map<String, T> getMap() {
        if (map == null) {
            map = list.stream().collect(Collectors.toMap(Identifiable::getId, Function.identity()));
        }
        return map;
    }

    @Override
    public Object getInvokeCollection() {
        return this;
    }
}
