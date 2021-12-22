package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;

/**
 * 持有数据源集合的数据源 / A source that holds a collection of sources
 */
@Getter
@AllArgsConstructor
public class MultipleSource implements Source {

    @NonNull
    private final Collection<Source> sources;

    /**
     * 如果集合为空返回null / Returns null if the collection is empty
     * 否则返回第一个有名称的数据源的名称 / Otherwise, the name of the first named source is returned
     *
     * @return 名称 / Name
     */
    @Override
    public String getName() {
        if (!sources.isEmpty()) {
            for (Source source : sources) {
                String name = source.getName();
                if (name != null && !name.isEmpty()) {
                    return name;
                }
            }
        }
        return null;
    }

    /**
     * 如果集合只有一个下载源对象 / If the collection has only one source
     * 则使用这个下载源的编码 / Use the charset of this source
     * 否则返回null / Otherwise, null is returned
     *
     * @return 编码 / Charset
     */
    @Override
    public Charset getCharset() {
        Set<Charset> charsets = new HashSet<>();
        for (Source source : sources) {
            charsets.add(source.getCharset());
        }
        int size = sources.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return charsets.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * 集合中所有下载源的字节数总和 / The sum of bytes of all sources in the collection
     *
     * @return 字节数 / bytes count
     */
    @Override
    public long getLength() {
        long length = 0;
        for (Source source : sources) {
            length += source.getLength();
        }
        return length;
    }

    /**
     * 只要集合中有一个启用就启用 / Enabled whenever one in the collection is enabled
     *
     * @return 是否启用缓存 / If enable cache
     */
    @Override
    public boolean isCacheEnabled() {
        for (Source source : sources) {
            if (source.isCacheEnabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回null / Return null
     *
     * @return 缓存路径 / The path of cache
     */
    @Override
    public String getCachePath() {
        return null;
    }

    /**
     * 集合中只有一个下载源并且这个下载源是单个的才返回true / True is returned only if there is only one source in the collection and the source is single
     *
     * @return 是否是单个的 / If single
     */
    @Override
    public boolean isSingle() {
        Boolean single = null;
        for (Source source : sources) {
            if (single == null) {
                single = source.isSingle();
                if (!single) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 对集合中的所有下载源都执行加载 / Load all sources in the collection
     *
     * @param context 下载上下文 / Context of download
     * @throws IOException I/O exception
     */
    @Override
    public void load(DownloadContext context) throws IOException {
        for (Source source : sources) {
            source.load(context);
        }
    }

    /**
     * 集合中的下载源只要有一个异步加载就是异步加载 / As long as there is one source load async in the collection, it is load async
     *
     * @return 是否异步加载 / If async load
     */
    @Override
    public boolean isAsyncLoad() {
        for (Source source : sources) {
            if (source.isAsyncLoad()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对集合中的所有下载源都执行写入 / Write all sources in the collection
     *
     * @param os      写入数据的输出流 / Output stream to write
     * @param range   写入的范围 / Range of writing
     * @param writer  具体操作字节或字符的处理类 / Handler to handle bytes or chars
     * @param handler 可对每一部分进行单独写入操作 / Do write for each part {@link Part}
     * @throws IOException I/O exception
     */
    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        for (Source source : sources) {
            source.write(os, range, writer, handler);
        }
    }

    /**
     * 筛选所有符合条件的下载源 / Filter all qualified download sources
     * 并且将深层结构的下载源都提取放在一个集合中 / Extract the download sources of deep structure into a collection
     *
     * @param predicate 过滤条件 / Filter condition
     * @return 列出的所有符合条件的数据源 / All sources which is qualified
     */
    @Override
    public Collection<Source> list(Predicate<Source> predicate) {
        List<Source> all = new ArrayList<>();
        for (Source source : sources) {
            all.addAll(source.list(predicate));
        }
        return all;
    }

    /**
     * 对集合中的所有下载源都删除缓存 / Delete all sources cache in the collection
     */
    @Override
    public void deleteCache() {
        sources.forEach(Cacheable::deleteCache);
    }

    @Override
    public String toString() {
        return "MultipleSource{" +
                "sources=" + sources +
                '}';
    }
}
