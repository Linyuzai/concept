package com.github.linyuzai.plugin.autoconfigure.preperties;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 配置
 */
@Data
@ConfigurationProperties(prefix = "concept.plugin")
public class PluginConceptProperties {

    /**
     * 插件元数据配置
     */
    private MetadataProperties metadata = new MetadataProperties();

    /**
     * 插件校验配置
     */
    private ValidationProperties validation = new ValidationProperties();

    /**
     * 插件存储配置
     */
    private StorageProperties storage = new StorageProperties();

    /**
     * 插件扩展配置
     */
    private ExtensionProperties extension = new ExtensionProperties();

    /**
     * 自动加载配置
     */
    private AutoloadProperties autoload = new AutoloadProperties();

    /**
     * 日志配置
     */
    private LoggerProperties logger = new LoggerProperties();

    /**
     * 管理配置
     */
    private ManagementProperties management = new ManagementProperties();

    /**
     * 元数据配置
     */
    @Data
    public static class MetadataProperties {

        /**
         * 标准配置类型
         */
        private Class<? extends Plugin.StandardMetadata> standardType = JarPlugin.StandardMetadata.class;
    }

    /**
     * 校验配置
     */
    @Data
    public static class ValidationProperties {

        private DataSize maxReadSize = DataSize.ofBytes(-1);

        private int maxNestedDepth = -1;
    }

    /**
     * 插件存储配置
     */
    @Data
    public static class StorageProperties {

        /**
         * 插件存储类型
         */
        private StorageType type = StorageType.MEMORY;

        /**
         * 插件存储基础路径
         */
        private String location;

        private Set<String> filterSuffixes = new LinkedHashSet<>();

        public enum StorageType {

            MEMORY, LOCAL, MINIO, AWS_V1, AWS_V2
        }
    }

    @Data
    public static class ExtensionProperties {

        private RequestMappingProperties requestMapping = new RequestMappingProperties();

        @Data
        public static class RequestMappingProperties {

            private boolean enabled;
        }
    }

    /**
     * 自动加载配置
     */
    @Data
    public static class AutoloadProperties {

        /**
         * 是否启用自动加载
         */
        private boolean enabled = true;

        /**
         * 自动加载器轮训间隔ms，默认5000
         */
        private long period = 5000;
    }

    /**
     * 日志配置
     */
    @Data
    public static class LoggerProperties {

        /**
         * 标准日志配置
         */
        private StandardProperties standard = new StandardProperties();

        /**
         * 标准日志配置
         */
        @Data
        public static class StandardProperties {

            /**
             * 是否启用标准日志配置
             */
            private boolean enabled = true;
        }
    }

    /**
     * 管理配置
     */
    @Data
    public static class ManagementProperties {

        /**
         * 是否启用管理页面
         */
        private boolean enabled = true;

        /**
         * 权限配置
         */
        private AuthorizationProperties authorization = new AuthorizationProperties();

        /**
         * Github角配置
         */
        private GithubCornerProperties githubCorner = new GithubCornerProperties();

        /**
         * 头部配置
         */
        private HeaderProperties header = new HeaderProperties();

        /**
         * 底部配置
         */
        private FooterProperties footer = new FooterProperties();

        /**
         * 权限配置
         */
        @Data
        public static class AuthorizationProperties {

            /**
             * 解锁密码
             */
            private String password;
        }

        /**
         * Github角配置
         */
        @Data
        public static class GithubCornerProperties {

            /**
             * 是否展示Github角配置
             */
            private boolean display = true;
        }

        /**
         * 头部配置
         */
        @Data
        public static class HeaderProperties {

            /**
             * 是否展示头部
             */
            private boolean display = true;

            /**
             * 头部标题配置
             */
            private TitleProperties title = new TitleProperties();

            /**
             * 头部标题配置
             */
            @Data
            public static class TitleProperties {

                /**
                 * 是否展示头部标题
                 */
                private boolean display = true;

                /**
                 * 头部标题内容
                 */
                private String text = "Concept Plugin";
            }
        }

        /**
         * 底部配置
         */
        @Data
        public static class FooterProperties {

            /**
             * 是否展示底部
             */
            private boolean display = true;
        }
    }
}
