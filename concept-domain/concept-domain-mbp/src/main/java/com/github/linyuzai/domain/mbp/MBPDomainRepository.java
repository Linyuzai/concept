package com.github.linyuzai.domain.mbp;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.linyuzai.domain.core.AbstractDomainRepository;
import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import com.github.linyuzai.domain.core.Identifiable;
import com.github.linyuzai.domain.core.condition.Conditions;
import com.github.linyuzai.domain.core.link.DomainLink;
import com.github.linyuzai.domain.core.page.Pages;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于 MyBatis-Plus 的通用存储
 *
 * @param <T> 领域模型
 * @param <P> 数据模型
 */
public abstract class MBPDomainRepository<T extends DomainObject, C extends DomainCollection<T>, P extends Identifiable>
        extends AbstractDomainRepository<T, C, P> {

    /**
     * 插入一条数据
     */
    @Override
    protected void doCreate(P po) {
        getBaseMapper().insert(po);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void create(C collection) {
        super.create(collection);
    }

    /**
     * 插入多条数据
     */
    @Override
    protected void doCreate(Collection<? extends P> pos) {
        pos.forEach(this::doCreate);
    }

    /**
     * 更新一条数据
     */
    @Override
    protected void doUpdate(P po) {
        getBaseMapper().updateById(po);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void update(C collection) {
        super.update(collection);
    }

    /**
     * 更新多条数据
     */
    @Override
    protected void doUpdate(Collection<? extends P> pos) {
        pos.forEach(this::doUpdate);
    }

    /**
     * 删除一条数据
     */
    @Override
    protected void doDelete(P po) {
        getBaseMapper().deleteById(po.getId());
    }

    @Override
    protected void doDelete(Collection<? extends P> pos) {
        getBaseMapper().deleteBatchIds(pos.stream().map(Identifiable::getId).collect(Collectors.toSet()));
    }

    /**
     * 根据 id 获得一条数据
     */
    @Override
    protected P doGet(String id) {
        return getBaseMapper().selectById(id);
    }

    /**
     * 根据 id 集合获得多条数据
     */
    @Override
    protected Collection<P> doSelect(Collection<String> ids) {
        return getBaseMapper().selectBatchIds(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void delete(Conditions conditions) {
        super.delete(conditions);
    }

    /**
     * 条件删除数据
     */
    @Override
    protected void doDelete(Conditions conditions) {
        getBaseMapper().delete(getWrapper(conditions));
    }

    /**
     * 根据条件查询一条数据
     */
    @Override
    protected P doGet(Conditions conditions) {
        return getBaseMapper().selectOne(getWrapper(conditions));
    }

    @Override
    protected Collection<P> doSelect(Conditions conditions) {
        return getBaseMapper().selectList(getWrapper(conditions));
    }

    /**
     * 根据条件获得数量
     */
    @Override
    protected Long doCount(Conditions conditions) {
        return getBaseMapper().selectCount(getWrapper(conditions));
    }

    /**
     * 根据条件获得分页数据
     */
    @Override
    protected Pages<P> doPage(Conditions conditions, Pages.Args page) {
        IPage<P> p = new Page<>(page.getCurrent(), page.getSize());
        return toPages(getBaseMapper().selectPage(p, getWrapper(conditions)));
    }

    /**
     * 根据条件生成 Wrapper
     */
    protected Wrapper<P> getWrapper(Conditions conditions) {
        QueryWrapper<P> wrapper = new QueryWrapper<>();
        conditions.getEquals().forEach(it ->
                wrapper.eq(fetchColumn(getFetchClass(), it.getKey()), it.getValue()));
        conditions.getNulls().forEach(it ->
                wrapper.isNull(fetchColumn(getFetchClass(), it.getKey())));
        conditions.getIns().forEach(it ->
                wrapper.in(fetchColumn(getFetchClass(), it.getKey()), it.getValues()));
        conditions.getLikes().forEach(it ->
                wrapper.like(fetchColumn(getFetchClass(), it.getKey()), it.getValue()));
        conditions.getOrderBys().forEach(it -> {
            if (it.isDesc()) {
                wrapper.orderByDesc(fetchColumn(getFetchClass(), it.getKey()));
            } else {
                wrapper.orderByAsc(fetchColumn(getFetchClass(), it.getKey()));
            }
        });
        return wrapper;
    }

    /**
     * 匹配字段，借助 MyBatis-Plus 的工具获得实体字段与数据库字段的映射
     */
    protected String fetchColumn(Class<P> clazz, String field) {
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(clazz);
        ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(field));
        if (columnCache == null) {
            return field;
        }
        return columnCache.getColumn();
    }

    /**
     * 将 mbp 的 page 转为我们的领域 pages
     */
    protected Pages<P> toPages(IPage<P> p) {
        Pages<P> pages = new Pages<>();
        pages.setCurrent(p.getCurrent());
        pages.setSize(p.getSize());
        pages.setTotal(p.getTotal());
        pages.setPages(p.getPages());
        pages.setRecords(p.getRecords());
        return pages;
    }

    /**
     * 字段匹配类
     */
    public Class<P> getFetchClass() {
        return DomainLink.generic(getClass(), 0);
    }

    /**
     * 获得 Mapper
     */
    public abstract BaseMapper<P> getBaseMapper();
}
