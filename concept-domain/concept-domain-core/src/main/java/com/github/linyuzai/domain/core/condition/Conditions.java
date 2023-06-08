package com.github.linyuzai.domain.core.condition;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 查询条件
 */
@ToString
@Getter
@Setter(AccessLevel.PROTECTED)
public class Conditions {

    public static final String ID = "id";

    public static final Conditions EMPTY = new Conditions();

    /**
     * 如果 key 是 null 则不拼接
     */
    private boolean ignoreIfNullKey = true;

    /**
     * 如果 key 是 空字符串 则不拼接
     */
    private boolean ignoreIfEmptyKey = true;

    /**
     * = 条件
     */
    private Collection<Equal> equals = new ArrayList<>();

    /**
     * is null 条件
     */
    private Collection<Null> nulls = new ArrayList<>();

    /**
     * in 条件
     */
    private Collection<In> ins = new ArrayList<>();

    /**
     * like 条件
     */
    private Collection<Like> likes = new ArrayList<>();

    /**
     * order by 条件
     */
    private Collection<OrderBy> orderBys = new ArrayList<>();

    /**
     * limit
     */
    private Limit limit;

    public static Conditions from(Conditions source) {
        if (source == EMPTY) {
            return EMPTY;
        }
        Conditions conditions = new Conditions();
        source.copy(conditions);
        return conditions;
    }

    public static Conditions id(String id) {
        return new Conditions().equal(ID, id);
    }

    public static Conditions ids(Collection<String> ids) {
        return new Conditions().in(ID, ids);
    }

    //有需要可以继承添加其他条件

    public LambdaConditions lambda() {
        LambdaConditions conditions = new LambdaConditions();
        copy(conditions);
        return conditions;
    }

    private void copy(Conditions conditions) {
        conditions.setIgnoreIfNullKey(this.ignoreIfNullKey);
        conditions.setIgnoreIfEmptyKey(this.ignoreIfEmptyKey);
        conditions.getEquals().addAll(this.equals);
        conditions.getNulls().addAll(this.nulls);
        conditions.getIns().addAll(this.ins);
        conditions.getLikes().addAll(this.likes);
        conditions.getOrderBys().addAll(this.orderBys);
        conditions.setLimit(this.limit);
    }

    public Conditions ignoreIfNullKey(boolean ignore) {
        this.ignoreIfNullKey = ignore;
        return this;
    }

    public Conditions ignoreIfEmptyKey(boolean ignore) {
        this.ignoreIfEmptyKey = ignore;
        return this;
    }

    /**
     * 添加 =
     */
    public Conditions equal(String key, Object value) {
        if (notIgnore(key)) {
            equals.add(new Equal(key, value));
        }
        return this;
    }

    /**
     * 添加 is null
     */
    public Conditions isNull(String key) {
        return isNull(key, false);
    }

    /**
     * 添加 is null
     */
    public Conditions isNotNull(String key) {
        return isNull(key, true);
    }

    /**
     * 添加 null 条件
     */
    public Conditions isNull(String key, boolean not) {
        if (notIgnore(key)) {
            nulls.add(new Null(key, not));
        }
        return this;
    }

    /**
     * 添加 in
     */
    public Conditions in(String key, Collection<?> values) {
        if (notIgnore(key)) {
            ins.add(new In(key, values));
        }
        return this;
    }

    /**
     * 添加 like
     */
    public Conditions like(String key, String value) {
        if (notIgnore(key)) {
            likes.add(new Like(key, value));
        }
        return this;
    }

    /**
     * 添加 order by asc
     */
    public Conditions orderBy(String key) {
        return orderBy(key, false);
    }

    /**
     * 添加 order by
     */
    public Conditions orderByDesc(String key) {
        return orderBy(key, true);
    }

    /**
     * 添加 order by
     */
    public Conditions orderBy(String key, boolean desc) {
        if (notIgnore(key)) {
            orderBys.add(new OrderBy(key, desc));
        }
        return this;
    }

    /**
     * 设置 limit
     */
    public Conditions limit(long start, long size) {
        limit = new Limit(start, size);
        return this;
    }

    /**
     * 设置 limit
     */
    public Conditions limit(long size) {
        limit = new Limit(0, size);
        return this;
    }

    protected boolean notIgnore(Object value) {
        if (ignoreIfNullKey) {
            if (value == null) {
                return false;
            }
        }
        if (ignoreIfEmptyKey) {
            if (value instanceof CharSequence) {
                if (((CharSequence) value).length() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * = 条件
     */
    @ToString
    @Getter
    @AllArgsConstructor
    @Setter(AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Equal {

        /**
         * = 的 key
         */
        private String key;

        /**
         * = 的 value
         */
        private Object value;
    }

    /**
     * is null 条件
     */
    @ToString
    @Getter
    @AllArgsConstructor
    @Setter(AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Null {

        /**
         * is null 的 key
         */
        private String key;

        private boolean not;
    }

    /**
     * in 条件
     */
    @ToString
    @Getter
    @AllArgsConstructor
    @Setter(AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class In {

        /**
         * in 的 key
         */
        private String key;

        /**
         * in 的 values
         */
        private Collection<?> values;
    }

    /**
     * like 条件
     */
    @ToString
    @Getter
    @AllArgsConstructor
    @Setter(AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Like {

        /**
         * like 的 key
         */
        private String key;

        /**
         * like 的 value
         */
        private String value;
    }

    /**
     * order by 条件
     */
    @ToString
    @Getter
    @AllArgsConstructor
    @Setter(AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderBy {

        /**
         * order by 的 key
         */
        private String key;

        /**
         * order by 是否倒序
         */
        private boolean desc;
    }

    /**
     * limit 条件
     */
    @ToString
    @Getter
    @AllArgsConstructor
    @Setter(AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Limit {

        /**
         * limit 开始下标
         */
        private long start;

        /**
         * limit 数量
         */
        private long size;
    }
}
