package com.github.linyuzai.domain.core.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Schema(description = "分页视图")
public class Pages<T> {

    @Schema(description = "分页视图")
    private Collection<T> records = Collections.emptyList();

    @Schema(description = "总数")
    private long total = 0;

    @Schema(description = "每页数量")
    private long size = 10;

    @Schema(description = "当前页数")
    private long current = 1;

    @Schema(description = "总页数")
    private long pages = 1;

    /**
     * 记录模型转换
     */
    public <R> Pages<R> map(Function<T, R> function) {
        Pages<R> p = new Pages<>();
        p.setTotal(this.total);
        p.setSize(this.size);
        p.setCurrent(this.current);
        p.setPages(this.pages);
        p.setRecords(this.records.stream()
                .map(function)
                .collect(Collectors.toList()));
        return p;
    }

    public <R> Pages<R> mapAll(Function<Collection<T>, Collection<R>> function) {
        Pages<R> p = new Pages<>();
        p.setTotal(this.total);
        p.setSize(this.size);
        p.setCurrent(this.current);
        p.setPages(this.pages);
        p.setRecords(function.apply(this.records));
        return p;
    }

    @Data
    @Schema(description = "分页参数")
    public static class Args {

        @Schema(description = "当前页数")
        private long current = 1;

        @Schema(description = "每页数量")
        private long size = 10;
    }
}
