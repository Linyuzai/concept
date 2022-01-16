package com.github.linyuzai.download.core.configuration;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 下载配置 / Configuration of download
 * 全局默认配置 / Global default configuration
 */
@Data
@ConfigurationProperties(prefix = "concept.download")
public class DownloadConfiguration {

    private ResponseConfiguration response = new ResponseConfiguration();

    private SourceConfiguration source = new SourceConfiguration();

    private CompressConfiguration compress = new CompressConfiguration();

    @Data
    public static class ResponseConfiguration {

        private Map<String, String> headers;
    }

    @Data
    public static class SourceConfiguration {

        private CacheConfiguration cache = new CacheConfiguration();
    }

    @Data
    public static class CompressConfiguration {

        private String format = CompressFormat.ZIP;

        private CacheConfiguration cache = new CacheConfiguration();
    }

    @Data
    public static class CacheConfiguration {

        private boolean enabled;

        private String path = Cacheable.PATH;

        private boolean delete;
    }
}
