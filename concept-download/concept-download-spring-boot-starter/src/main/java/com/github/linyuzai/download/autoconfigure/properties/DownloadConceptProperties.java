package com.github.linyuzai.download.autoconfigure.properties;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.contenttype.ContentType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Deprecated
@Data
//@ConfigurationProperties(prefix = "concept.download")
public class DownloadConceptProperties {

    private ResponseProperties response = new ResponseProperties();

    private SourceProperties source = new SourceProperties();

    private CompressProperties compress = new CompressProperties();

    @Data
    public static class ResponseProperties {

        /**
         * 额外的响应头 / Additional response headers
         */
        private Map<String, String> headers;
    }

    @Data
    public static class SourceProperties {

        private CacheProperties cache = new CacheProperties();
    }

    @Data
    public static class CompressProperties {

        /**
         * 压缩格式 / Compression format
         */
        private String format = CompressFormat.ZIP;

        private CacheProperties cache = new CacheProperties();
    }

    @Data
    public static class CacheProperties {

        /**
         * 是否开启缓存 / If cache is enabled
         */
        private boolean enabled;

        /**
         * 缓存地址 / Path of cache
         */
        private String path = Cacheable.PATH;

        /**
         * 下载结束后缓存是否删除 / If delete cache after downloading
         */
        private boolean delete;
    }
}
