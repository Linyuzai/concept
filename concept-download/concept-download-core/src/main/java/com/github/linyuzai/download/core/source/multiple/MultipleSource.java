package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;

/**
 * 持有数据源集合的数据源 / A source that holds a collection of sources
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MultipleSource implements Source {

    @NonNull
    protected Collection<Source> sources;

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * 如果集合为空返回null / Returns null if the collection is empty
     * 否则返回第一个有名称的数据源的名称 / Otherwise, the name of the first named source is returned
     *
     * @return 名称 / Name
     */
    @Override
    public String getName() {
        for (Source source : sources) {
            String name = source.getName();
            if (name != null && !name.isEmpty()) {
                return name;
            }
        }
        return null;
    }

    @Override
    public String getContentType() {
        Set<String> contentTypes = new HashSet<>();
        for (Source source : sources) {
            contentTypes.add(source.getContentType());
        }
        if (contentTypes.size() == 1) {
            return contentTypes.iterator().next();
        } else {
            return null;
        }
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
        if (charsets.size() == 1) {
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
    public Long getLength() {
        long length = 0;
        for (Source source : sources) {
            Long l = source.getLength();
            if (l == null || l < 0) {
                return null;
            }
            length += l;
        }
        return length;
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

    @Override
    public Collection<Part> getParts() {
        Collection<Part> parts = new ArrayList<>();
        for (Source source : sources) {
            parts.addAll(source.getParts());
        }
        return parts;
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
