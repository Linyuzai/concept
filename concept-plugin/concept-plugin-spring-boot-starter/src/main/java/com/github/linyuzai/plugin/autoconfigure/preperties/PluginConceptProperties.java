package com.github.linyuzai.plugin.autoconfigure.preperties;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置
 */
@Data
@ConfigurationProperties(prefix = "concept.plugin")
public class PluginConceptProperties {

    /**
     * 元数据配置
     */
    private MetadataProperties metadata = new MetadataProperties();

    /**
     * 插件目录
     */
    private StorageProperties storage = new StorageProperties();

    /**
     * 自动加载配置
     */
    private AutoloadProperties autoload = new AutoloadProperties();

    /**
     * 日志配置
     */
    private LoggerProperties logger = new LoggerProperties();

    /**
     * jar插件配置
     */
    private JarProperties jar = new JarProperties();

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
     * 插件目录
     */
    @Data
    public static class StorageProperties {

        private StorageType type = StorageType.LOCAL;

        /**
         * 自动加载插件目录基础路径
         */
        private String location;

        public enum StorageType {

            LOCAL, MINIO, AWS_V1, AWS_V2
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
         * 异常日志配置
         */
        private ErrorProperties error = new ErrorProperties();

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

        /**
         * 异常日志配置
         */
        @Data
        public static class ErrorProperties {

            /**
             * 是否启用异常日志配置
             */
            private boolean enabled = true;
        }
    }

    /**
     * jar插件配置
     */
    @Data
    public static class JarProperties {

        /**
         * jar插件模式
         */
        private Mode mode = Mode.STREAM;

        /**
         * jar插件模式
         */
        public enum Mode {

            /**
             * 数据流
             */
            STREAM,

            /**
             * 文件随机访问
             */
            FILE
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
