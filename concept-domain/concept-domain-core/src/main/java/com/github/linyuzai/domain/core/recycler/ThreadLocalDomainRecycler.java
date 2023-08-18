package com.github.linyuzai.domain.core.recycler;

import com.github.linyuzai.domain.core.DomainObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class ThreadLocalDomainRecycler implements DomainRecycler {

    private final ThreadLocal<Map<Object, Map<Class<? extends DomainObject>, List<DomainObject>>>> TL =
            ThreadLocal.withInitial(HashMap::new);

    private final DomainRecycler recycler;

    @SuppressWarnings("unchecked")
    public void recycle() {
        Map<Object, Map<Class<? extends DomainObject>, List<DomainObject>>> map = TL.get();
        map.forEach((recycleType, listMap) ->
                listMap.forEach((domainType, list) ->
                        list.forEach(recyclable ->
                                recycle(recycleType, (Class<DomainObject>) domainType, recyclable))));
        TL.remove();
    }

    @Override
    public <T extends DomainObject> boolean recycle(Object recycleType, Class<T> domainType, T recyclable) {
        return recycler.recycle(recycleType, domainType, recyclable);
    }

    @Override
    public <T extends DomainObject> T reuse(Object recycleType, Class<T> domainType, Supplier<T> supplier) {
        T reused = recycler.reuse(recycleType, domainType, supplier);
        TL.get().computeIfAbsent(recycleType, rt -> new HashMap<>())
                .computeIfAbsent(domainType, dt -> new ArrayList<>())
                .add(reused);
        return reused;
    }
}
