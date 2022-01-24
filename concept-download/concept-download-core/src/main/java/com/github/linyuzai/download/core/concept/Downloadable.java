package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.source.Source;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 抽象 {@link Source} 和 {@link Compression} 的通用部分。
 * <p>
 * Abstract the common parts of {@link Source} and {@link Compression}.
 */
public interface Downloadable extends Part, Cacheable {

    /**
     * 获得描述。
     * <p>
     * Get description.
     *
     * @return 描述
     * <p>
     * Description
     */
    String getDescription();

    /**
     * 获得所有的 {@link Part}。
     * <p>
     * Get all {@link Part}.
     *
     * @return 所有的 {@link Part}
     * <p>
     * All {@link Part}
     */
    default Collection<Part> getParts() {
        Collection<Part> parts = new ArrayList<>();
        addPart(this, parts);
        return parts;
    }

    /**
     * 递归获得所有的 {@link Part}。
     * <p>
     * Get all {@link Part} recursively.
     *
     * @param part  {@link Part}
     * @param parts 集合容器
     *              <p>
     *              Collection container
     */
    static void addPart(Part part, Collection<Part> parts) {
        parts.add(part);
        for (Part child : part.getChildren()) {
            addPart(child, parts);
        }
    }
}
