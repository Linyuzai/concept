package com.github.linyuzai.domain.core;

import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.page.Pages;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 领域存储抽象类
 *
 * @param <T> 领域模型
 * @param <P> 数据模型
 */
public abstract class AbstractDomainRepository<T extends DomainObject, C extends DomainCollection<T>,
        P extends Identifiable> implements DomainRepository<T, C> {

    /**
     * 领域模型转数据模型
     */
    public abstract P do2po(T object);

    public Collection<P> dos2pos(Collection<? extends T> objects) {
        return objects.stream().map(this::do2po).collect(Collectors.toList());
    }

    /**
     * 数据模型转领域模型
     */
    public abstract T po2do(P po);

    public Collection<T> pos2dos(Collection<? extends P> pos) {
        return pos.stream().map(this::po2do).collect(Collectors.toList());
    }

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
    public void create(C collection) {
        doCreate(dos2pos(collection.list()));
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
    }

    /**
     * 更新一条记录
     */
    protected abstract void doUpdate(P po);

    /**
     * 更新多个领域模型
     */
    @Override
    public void update(C collection) {
        doUpdate(dos2pos(collection.list()));
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
    }

    /**
     * 删除一条记录
     */
    protected abstract void doDelete(P po);

    @Override
    public void delete(C collection) {
        doDelete(dos2pos(collection.list()));
    }

    protected abstract void doDelete(Collection<? extends P> pos);

    /**
     * 根据 id 获得一个领域模型
     */
    @Override
    public T get(String id) {
        P po = doGet(id);
        if (po == null) {
            return null;
        }
        return po2do(po);
    }

    /**
     * 根据 id 获得一条数据
     */
    protected abstract P doGet(String id);

    /**
     * 根据 ids 获得多个领域模型
     */
    @Override
    public C select(Collection<String> ids) {
        return wrap(pos2dos(doSelect(ids)));
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
    }

    /**
     * 条件删除
     */
    protected abstract void doDelete(Conditions conditions);

    /**
     * 条件查询
     */
    @Override
    public T get(Conditions conditions) {
        P po = doGet(conditions);
        if (po == null) {
            return null;
        }
        return po2do(po);
    }

    /**
     * 条件查询
     */
    protected abstract P doGet(Conditions conditions);

    @Override
    public C select(Conditions conditions) {
        return wrap(pos2dos(doSelect(conditions)));
    }

    protected abstract Collection<P> doSelect(Conditions conditions);

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
     * 条件查询分页
     */
    @Override
    public Pages<T> page(Conditions conditions, Pages.Args page) {
        return doPage(conditions, page).mapAll(this::pos2dos);
    }

    /**
     * 条件查询分页
     */
    protected abstract Pages<P> doPage(Conditions conditions, Pages.Args page);

    protected C wrap(Collection<T> objects) {
        return DomainCollection.wrap(getGenericType(), objects);
    }

    protected Class<C> getGenericType() {
        return DomainLink.generic(getClass(), 1);
    }
}