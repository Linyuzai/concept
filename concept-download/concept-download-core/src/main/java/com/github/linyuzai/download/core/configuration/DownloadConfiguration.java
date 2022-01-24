package com.github.linyuzai.download.core.configuration;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 下载配置。
 * <p>
 * Configuration of download.
 */
@Data
@ConfigurationProperties(prefix = "concept.download")
public class DownloadConfiguration {

    private ResponseConfiguration response = new ResponseConfiguration();

    private SourceConfiguration source = new SourceConfiguration();

    private CompressConfiguration compress = new CompressConfiguration();

    @Data
    public static class ResponseConfiguration {

        /**
         * 额外的响应头。
         * <p>
         * Additional response headers.
         */
        private Map<String, String> headers;
    }

    @Data
    public static class SourceConfiguration {

        private CacheConfiguration cache = new CacheConfiguration();
    }

    @Data
    public static class CompressConfiguration {

        /**
         * 压缩格式。
         * <p>
         * Compression format.
         */
        private String format = CompressFormat.ZIP;

        private CacheConfiguration cache = new CacheConfiguration();
    }

    @Data
    public static class CacheConfiguration {

        /**
         * 是否启用缓存。
         * <p>
         * Enable caching.
         */
        private boolean enabled;

        /**
         * 缓存路径。
         * <p>
         * Cache path.
         */
        private String path = Cacheable.PATH;

        /**
         * 下载结束后是否删除缓存。
         * <p>
         * Delete cache after downloading.
         */
        private boolean delete;
    }
}
