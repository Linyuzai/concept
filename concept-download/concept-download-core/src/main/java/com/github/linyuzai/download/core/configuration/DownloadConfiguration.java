package com.github.linyuzai.download.core.configuration;

import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 全局下载配置。
 */
@Data
@ConfigurationProperties(prefix = "concept.download")
public class DownloadConfiguration {

    private ResponseConfiguration response = new ResponseConfiguration();

    private SourceConfiguration source = new SourceConfiguration();

    private CompressConfiguration compress = new CompressConfiguration();

    private LoggerConfiguration logger = new LoggerConfiguration();

    @Data
    public static class ResponseConfiguration {

        /**
         * 额外的响应头
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
         * 压缩格式
         */
        private String format = CompressFormat.ZIP;

        private CacheConfiguration cache = new CacheConfiguration();
    }

    @Data
    public static class CacheConfiguration {

        /**
         * 是否启用缓存
         */
        private boolean enabled;

        /**
         * 缓存路径
         */
        private String path = Cacheable.PATH;

        /**
         * 下载结束后是否删除缓存。
         */
        private boolean delete;
    }

    @Data
    public static class LoggerConfiguration {

        /**
         * 是否启用日志
         */
        private boolean enabled;

        private StandardLoggerConfiguration standard = new StandardLoggerConfiguration();

        private TimeSpentLoggerConfiguration timeSpent = new TimeSpentLoggerConfiguration();

        private ProgressLoggerConfiguration progress = new ProgressLoggerConfiguration();
    }

    @Data
    public static class StandardLoggerConfiguration {

        /**
         * 是否启用标准日志
         */
        private boolean enabled = true;
    }

    @Data
    public static class TimeSpentLoggerConfiguration {

        /**
         * 是否启用事件计算日志
         */
        private boolean enabled = true;
    }

    @Data
    public static class ProgressLoggerConfiguration {

        /**
         * 是否启用进度计算日志
         */
        private boolean enabled = true;

        /**
         * 间隔，ms
         */
        private int duration = 1000;

        /**
         * 百分比计算
         */
        private boolean percentage;
    }
}
