package com.github.linyuzai.domain.core.recycler;

import com.github.linyuzai.domain.core.DomainObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class ThreadLocalDomainRecycler implements DomainRecycler {

    private final ThreadLocal<List<RecycleHolder>> TL = ThreadLocal.withInitial(ArrayList::new);

    private final DomainRecycler recycler;

    @SuppressWarnings("unchecked")
    public void recycle() {
        List<RecycleHolder> holders = TL.get();
        for (RecycleHolder holder : holders) {
            recycle(holder.recycleType, (Class<DomainObject>) holder.domainType, holder.recyclable);
        }
        TL.remove();
    }

    @Override
    public <T extends DomainObject> boolean recycle(Object recycleType, Class<T> domainType, T recyclable) {
        return recycler.recycle(recycleType, domainType, recyclable);
    }

    @Override
    public <T extends DomainObject> T reuse(Object recycleType, Class<T> domainType, Supplier<T> supplier) {
        T reused = recycler.reuse(recycleType, domainType, supplier);
        RecycleHolder holder = new RecycleHolder();
        holder.recycleType = recycleType;
        holder.domainType = domainType;
        holder.recyclable = reused;
        TL.get().add(holder);
        return reused;
    }

    static class RecycleHolder {

        Object recycleType;

        Class<? extends DomainObject> domainType;

        DomainObject recyclable;
    }
}
