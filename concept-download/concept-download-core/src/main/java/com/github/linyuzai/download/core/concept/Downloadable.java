package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.source.Source;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 抽象 {@link Source} 和 {@link Compression} 的通用部分。
 */
public interface Downloadable extends Part, Cacheable {

    /**
     * 获得描述。
     *
     * @return 描述
     */
    String getDescription();

    /**
     * 获得所有的 {@link Part}。
     *
     * @return 所有的 {@link Part}
     */
    default Collection<Part> getParts() {
        Collection<Part> parts = new ArrayList<>();
        addPart(this, parts);
        return parts;
    }

    /**
     * 递归获得所有的 {@link Part}。
     *
     * @param part  {@link Part}
     * @param parts 集合容器
     */
    static void addPart(Part part, Collection<Part> parts) {
        parts.add(part);
        for (Part child : part.getChildren()) {
            addPart(child, parts);
        }
    }
}
