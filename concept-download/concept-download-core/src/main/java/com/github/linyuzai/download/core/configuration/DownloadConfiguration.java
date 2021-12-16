package com.github.linyuzai.download.core.configuration;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.contenttype.ContentType;
import lombok.Data;

import java.util.Map;

/**
 * 下载配置 / Configuration of download
 * 全局默认配置 / Global default configuration
 */
@Data
public class DownloadConfiguration {

    private ResponseConfiguration response = new ResponseConfiguration();

    private SourceConfiguration source = new SourceConfiguration();

    private CompressConfiguration compress = new CompressConfiguration();

    @Data
    public static class ResponseConfiguration {

        private String contentType = ContentType.OCTET_STREAM;

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
