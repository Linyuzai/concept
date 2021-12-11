package com.github.linyuzai.download.autoconfigure.properties;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.contenttype.ContentType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "concept.download")
public class DownloadConceptProperties {

    private ResponseProperties response = new ResponseProperties();

    private SourceProperties source = new SourceProperties();

    private CompressProperties compress = new CompressProperties();

    @Data
    public static class ResponseProperties {

        private String contentType = ContentType.OCTET_STREAM;

        private Map<String, String> headers;
    }

    @Data
    public static class SourceProperties {

        private CacheProperties cache = new CacheProperties();
    }

    @Data
    public static class CompressProperties {

        private String format = CompressFormat.ZIP;

        private CacheProperties cache = new CacheProperties();
    }

    @Data
    public static class CacheProperties {

        private boolean enabled;

        private String path = Cacheable.PATH;

        private boolean delete;
    }
}
