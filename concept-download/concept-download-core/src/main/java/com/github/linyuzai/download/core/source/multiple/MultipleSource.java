package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.file.EmptyInputStream;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;

/**
 * 容器化的 {@link Source} 实现。
 */
@Getter
@RequiredArgsConstructor
public class MultipleSource implements Source {

    /**
     * 实际的 {@link Source} 集合
     */
    @NonNull
    protected final Collection<Source> sources;

    /**
     * 无法获得输入流。
     *
     * @return {@link EmptyInputStream}
     */
    @Override
    public InputStream getInputStream() {
        return new EmptyInputStream();
    }

    /**
     * 如果集合为空则返回 null，
     * 否则返回第一个有名称的 {@link Source} 的名称。
     *
     * @return 名称
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

    /**
     * 如果只有一个 {@link Source} 则返回该 {@link Source} 的 Content-Type，否则返回 null。
     *
     * @return Content-Type
     */
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
     * 如果只有一个 {@link Source} 则返回该 {@link Source} 的编码，否则返回 null。
     *
     * @return 编码
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
     * 所有 {@link Source} 的长度总和。
     *
     * @return 长度
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
     * 只要有一个异步加载就是异步加载。
     *
     * @return 是否异步加载
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
     * 只有一个 {@link Source} 并且 {@link Source#isSingle()} 返回 true。
     *
     * @return 是否是单个文件
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

    @Override
    public String getDescription() {
        List<String> descriptions = new ArrayList<>();
        Collection<Source> list = list();
        for (Source s : list) {
            descriptions.add(s.getDescription());
        }
        return descriptions.toString();
    }

    /**
     * 加载所有的 {@link Source}。
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void load(DownloadContext context) {
        for (Source source : sources) {
            source.load(context);
        }
    }

    /**
     * 合并所有 {@link Source} 的 {@link Part}。
     *
     * @return 所有的 {@link Part}
     */
    @Override
    public Collection<Part> getParts() {
        Collection<Part> parts = new ArrayList<>();
        for (Source source : sources) {
            parts.addAll(source.getParts());
        }
        return parts;
    }

    /**
     * 筛选所有符合条件的 {@link Source}，
     * 合并所有符合条件的 {@link Source} 的 {@link Part}。
     *
     * @param predicate 过滤条件
     * @return 所有符合条件的 {@link Source} 的 {@link Part}
     */
    @Override
    public List<Source> list(Predicate<Source> predicate) {
        List<Source> all = new ArrayList<>();
        for (Source source : sources) {
            all.addAll(source.list(predicate));
        }
        return all;
    }

    /**
     * 删除所有 {@link Source} 的缓存。
     */
    @Override
    public void deleteCache() {
        sources.forEach(Cacheable::deleteCache);
    }

    /**
     * 释放所有 {@link Source} 的资源。
     */
    @Override
    public void release() {
        sources.forEach(Resource::release);
    }
}
