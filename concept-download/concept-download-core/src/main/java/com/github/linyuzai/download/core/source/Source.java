package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.load.Loadable;
import com.github.linyuzai.download.core.source.multiple.MultipleSource;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * 下载的数据源，作为处理的最小单位 / The downloaded data source as the smallest unit of processing
 */
public interface Source extends Downloadable, Loadable {

    /**
     * 如果对于一个File对象 / If for a file object {@link java.io.File}
     * 是一个文件夹 / Is a folder
     * 那么将返回false / False will be returned
     * 因为一个文件夹里面可能有多个文件或子文件夹 / Because there may be multiple files or sub folders in a folder
     *
     * @return 是否是单个的 / If single
     */
    boolean isSingle();

    /**
     * 将深层结构的下载源都提取放在一个集合中 / Extract the download sources of deep structure into a collection
     *
     * @return 列出的所有的数据源 / All sources which is listed
     * @see MultipleSource
     */
    default Collection<Source> list() {
        return list(source -> true);
    }

    /**
     * 筛选所有符合条件的下载源 / Filter all qualified download sources
     * 并且将深层结构的下载源都提取放在一个集合中 / Extract the download sources of deep structure into a collection
     *
     * @param predicate 过滤条件 / Filter condition
     * @return 列出的所有符合条件的数据源 / All sources which is qualified
     * @see MultipleSource
     */
    default Collection<Source> list(Predicate<Source> predicate) {
        if (predicate.test(this)) {
            return Collections.singletonList(this);
        } else {
            return Collections.emptyList();
        }
    }
}
