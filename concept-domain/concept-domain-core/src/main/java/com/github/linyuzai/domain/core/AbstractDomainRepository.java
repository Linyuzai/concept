package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.cache.Cache;
import com.github.linyuzai.domain.core.cache.CacheProvider;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.page.Pages;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 领域存储抽象类
 *
 * @param <T> 领域模型
 * @param <P> 数据模型
 */
public abstract class AbstractDomainRepository<T extends DomainObject, P extends Identifiable> implements DomainRepository<T> {

    /**
     * 缓存提供者
     */
    @Getter
    @Setter
    protected CacheProvider cacheProvider;

    /**
     * 缓存
     */
    @Setter
    protected volatile Cache<P> cache;

    protected Cache<P> getCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    if (cacheProvider == null) {
                        cache = Cache.disabled();
                    } else {
                        cache = cacheProvider.get(this);
                        if (cache == null) {
                            cache = Cache.disabled();
                        }
                    }
                }
            }
        }
        return cache;
    }

    /**
     * 领域模型转数据模型
     */
    public abstract P do2po(T object);

    /**
     * 数据模型转领域模型
     */
    public abstract T po2do(P object);

    /**
     * 创建一个领域模型
     */
    @Override
    public void create(T object) {
        doCreate(do2po(object));
    }

    /**
     * 添加一条记录
     */
    protected abstract void doCreate(P po);

    /**
     * 创建多个领域模型
     */
    @Override
    public void create(Collection<? extends T> objects) {
        doCreate(objects.stream().map(this::do2po).collect(Collectors.toList()));
    }

    /**
     * 添加多条记录
     */
    protected abstract void doCreate(Collection<? extends P> pos);

    /**
     * 更新一个领域模型
     */
    @Override
    public void update(T object) {
        doUpdate(do2po(object));
        //更新的时候清除缓存
        getCache().remove(object.getId());
    }

    /**
     * 更新一条记录
     */
    protected abstract void doUpdate(P po);

    /**
     * 更新多个领域模型
     */
    @Override
    public void update(Collection<? extends T> objects) {
        doUpdate(objects.stream().map(this::do2po).collect(Collectors.toList()));
        //更新的时候清除缓存
        objects.stream().map(DomainObject::getId).forEach(getCache()::remove);
    }

    /**
     * 更新多条记录
     */
    protected abstract void doUpdate(Collection<? extends P> pos);

    /**
     * 删除一个领域模型
     */
    @Override
    public void delete(T object) {
        doDelete(do2po(object));
        //删除的时候清除缓存
        getCache().remove(object.getId());
    }

    /**
     * 删除一条记录
     */
    protected abstract void doDelete(P po);

    /**
     * 根据 id 删除一个领域模型
     */
    @Override
    public void delete(String id) {
        doDelete(id);
        //删除的时候清除缓存
        getCache().remove(id);
    }

    /**
     * 根据 id 删除一条数据
     */
    protected abstract void doDelete(String id);

    /**
     * 根据 ids 删除多个领域模型1
     */
    @Override
    public void delete(Collection<String> ids) {
        doDelete(ids);
        //删除的时候清除缓存
        ids.forEach(getCache()::remove);
    }

    /**
     * 根据 ids 删除多条数据
     */
    protected abstract void doDelete(Collection<String> ids);

    /**
     * 根据 id 获得一个领域模型
     */
    @Override
    public T get(String id) {
        //读取的时候先从缓存读
        //如果没有缓存则查询后放入缓存
        P cache = getCache().get(id);
        if (cache == null) {
            P po = doGet(id);
            if (po == null) {
                return null;
            }
            getCache().set(id, po);
            return po2do(po);
        }
        return po2do(cache);
    }

    /**
     * 根据 id 获得一条数据
     */
    protected abstract P doGet(String id);

    /**
     * 根据 ids 获得多个领域模型
     */
    @Override
    public Collection<T> select(Collection<String> ids) {
        Collection<P> select = new ArrayList<>();
        Collection<String> unCachedIds = new ArrayList<>();
        for (String id : ids) {
            P cache = getCache().get(id);
            if (cache == null) {
                //没有缓存的先保存 id
                unCachedIds.add(id);
            } else {
                //有缓存的直接用
                select.add(cache);
            }
        }
        //一次性查询没有缓存的ids
        Collection<P> pos = doSelect(unCachedIds);
        //把这些放到缓存中
        pos.forEach(it -> getCache().set(it.getId(), it));
        select.addAll(pos);
        return select
                .stream()
                .map(this::po2do)
                .collect(Collectors.toList());
    }

    /**
     * 根据 ids 获得多条数据
     */
    protected abstract Collection<P> doSelect(Collection<String> ids);

    /**
     * 条件删除
     */
    @Override
    public void delete(Conditions conditions) {
        doDelete(conditions);
        stream(conditions).map(Identifiable::getId).forEach(getCache()::remove);
    }

    /**
     * 条件删除
     */
    protected abstract void doDelete(Conditions conditions);

    /**
     * 条件查询
     */
    @Override
    public T query(Conditions conditions) {
        P po = doQuery(conditions);
        if (po == null) {
            return null;
        }
        return po2do(po);
    }

    /**
     * 条件查询
     */
    protected abstract P doQuery(Conditions conditions);

    /**
     * 数量查询
     */
    @Override
    public Long count(Conditions conditions) {
        return doCount(conditions);
    }

    /**
     * 数量查询
     */
    protected abstract Long doCount(Conditions conditions);

    /**
     * 条件查询列表
     */
    @Override
    public List<T> list(Conditions conditions) {
        return doList(conditions)
                .stream()
                .map(this::po2do)
                .collect(Collectors.toList());
    }

    /**
     * 条件查询列表
     */
    protected abstract List<P> doList(Conditions conditions);

    /**
     * 条件查询分页
     */
    @Override
    public Pages<T> page(Conditions conditions, Pages.Args page) {
        return doPage(conditions, page).map(this::po2do);
    }

    /**
     * 条件查询分页
     */
    protected abstract Pages<P> doPage(Conditions conditions, Pages.Args page);

    /**
     * 条件查询流
     */
    @Override
    public Stream<T> stream(Conditions conditions) {
        return doStream(conditions).map(this::po2do);
    }

    /**
     * 条件查询流
     */
    protected abstract Stream<P> doStream(Conditions conditions);
}