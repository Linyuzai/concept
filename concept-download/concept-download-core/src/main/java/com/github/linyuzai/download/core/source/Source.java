package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.Loadable;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * 需要下载的原始数据对象的抽象。
 */
public interface Source extends Resource, Loadable {

    /**
     * 默认直接返回本身。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    default void load(DownloadContext context) {

    }

    /**
     * 是否是单个文件。
     * 比如对于 {@link File#isFile()} 则为 true，
     * 对于 {@link File#isDirectory()} 则为 false。
     *
     * @return 如果是单个文件则返回 true
     */
    boolean isSingle();

    /**
     * 将树形结构的 {@link Source} 展开放在一个集合中。
     *
     * @return 列出的所有的 {@link Source}
     */
    default Collection<Source> list() {
        return list(source -> true);
    }

    /**
     * 筛选所有符合条件的 {@link Source}，
     * 并且将树形结构的 {@link Source} 展开放在一个集合中。
     *
     * @param predicate 过滤条件
     * @return 列出的所有符合条件的 {@link Source}
     */
    default Collection<Source> list(Predicate<Source> predicate) {
        if (predicate.test(this)) {
            return Collections.singletonList(this);
        } else {
            return Collections.emptyList();
        }
    }
}
