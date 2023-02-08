package com.github.linyuzai.domain.core.condition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 查询条件
 */
@Getter
public class Conditions {

    /**
     * 如果是 null 则不拼接
     */
    @Setter
    private boolean ignoreIfNull = true;

    /**
     * 如果是 空字符串或空集合 则不拼接
     */
    @Setter
    private boolean ignoreIfEmpty = true;

    /**
     * = 条件
     */
    private final Collection<Equal> equals = new ArrayList<>();

    /**
     * is null 条件
     */
    private final Collection<Null> nulls = new ArrayList<>();

    /**
     * in 条件
     */
    private final Collection<In> ins = new ArrayList<>();

    /**
     * like 条件
     */
    private final Collection<Like> likes = new ArrayList<>();

    /**
     * order by 条件
     */
    private final Collection<OrderBy> orderBys = new ArrayList<>();

    /**
     * limit
     */
    @Setter(AccessLevel.PROTECTED)
    private Limit limit;

    //有需要可以添加其他条件

    public LambdaConditions lambda() {
        LambdaConditions conditions = new LambdaConditions();
        conditions.setIgnoreIfNull(this.ignoreIfNull);
        conditions.setIgnoreIfEmpty(this.ignoreIfEmpty);
        conditions.getEquals().addAll(this.equals);
        conditions.getNulls().addAll(this.nulls);
        conditions.getIns().addAll(this.ins);
        conditions.getLikes().addAll(this.likes);
        conditions.getOrderBys().addAll(this.orderBys);
        conditions.setLimit(this.limit);
        return conditions;
    }

    /**
     * 添加 =
     */
    public Conditions equal(String key, Object value) {
        if (notIgnore(key) && notIgnore(value)) {
            equals.add(new Equal(key, value));
        }
        return this;
    }

    /**
     * 添加 is null
     */
    public Conditions isNull(String key) {
        if (notIgnore(key)) {
            nulls.add(new Null(key));
        }
        return this;
    }

    /**
     * 添加 in
     */
    public Conditions in(String key, Collection<?> values) {
        if (notIgnore(key) && notIgnore(values)) {
            ins.add(new In(key, values));
        }
        return this;
    }

    /**
     * 添加 like
     */
    public Conditions like(String key, String value) {
        if (notIgnore(key) && notIgnore(value)) {
            likes.add(new Like(key, value));
        }
        return this;
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
        if (ignoreIfNull) {
            if (value == null) {
                return false;
            }
        }
        if (ignoreIfEmpty) {
            if (value instanceof Collection) {
                if (((Collection<?>) value).isEmpty()) {
                    return false;
                }
            }
            if (value instanceof Map) {
                if (((Map<?, ?>) value).isEmpty()) {
                    return false;
                }
            }
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
    @Getter
    @AllArgsConstructor
    public static class Equal {

        /**
         * = 的 key
         */
        private final String key;

        /**
         * = 的 value
         */
        private final Object value;
    }

    /**
     * is null 条件
     */
    @Getter
    @AllArgsConstructor
    public static class Null {

        /**
         * is null 的 key
         */
        private String key;
    }

    /**
     * in 条件
     */
    @Getter
    @AllArgsConstructor
    public static class In {

        /**
         * in 的 key
         */
        private final String key;

        /**
         * in 的 values
         */
        private final Collection<?> values;
    }

    /**
     * like 条件
     */
    @Getter
    @AllArgsConstructor
    public static class Like {

        /**
         * like 的 key
         */
        private final String key;

        /**
         * like 的 value
         */
        private final String value;
    }

    /**
     * order by 条件
     */
    @Getter
    @AllArgsConstructor
    public static class OrderBy {

        /**
         * order by 的 key
         */
        private final String key;

        /**
         * order by 是否倒序
         */
        private final boolean desc;
    }

    /**
     * limit 条件
     */
    @Getter
    @AllArgsConstructor
    public static class Limit {

        /**
         * limit 开始下标
         */
        private final long start;

        /**
         * limit 数量
         */
        private final long size;
    }
}
