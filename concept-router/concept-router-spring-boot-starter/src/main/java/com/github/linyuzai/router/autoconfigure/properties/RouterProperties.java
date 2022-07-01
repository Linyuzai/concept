package com.github.linyuzai.router.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * 路由配置
 */
@Data
@ConfigurationProperties(prefix = "concept.router")
public class RouterProperties {

    /**
     * 是否启用路由
     */
    private boolean enabled = true;

    /**
     * 路由仓库配置
     */
    private RepositoryProperties repository = new RepositoryProperties();

    /**
     * 路由日志配置
     */
    private LoggerProperties logger = new LoggerProperties();

    /**
     * Banner配置
     */
    private BannerProperties banner = new BannerProperties();

    /**
     * 可视化配置
     */
    private ManagementProperties management = new ManagementProperties();

    @Data
    public static class RepositoryProperties {

        /**
         * 路由仓库类型
         */
        private RepositoryType type = RepositoryType.LOCAL;

        /**
         * 本地路由仓库配置
         */
        private LocalProperties local = new LocalProperties();

        public enum RepositoryType {

            /**
             * 内存
             */
            MEMORY,

            /**
             * 本地文件
             */
            LOCAL
        }

        @Data
        public static class LocalProperties {

            /**
             * 本地文件目录
             */
            private String path = new File(System.getProperty("user.home"), "concept/router").getAbsolutePath();
        }
    }

    @Data
    public static class LoggerProperties {

        /**
         * 是否启用路由日志
         */
        private boolean enabled = true;
    }

    @Data
    public static class BannerProperties {

        /**
         * 是否打印Banner
         */
        private boolean enabled = true;
    }

    @Data
    public static class ManagementProperties {

        /**
         * 是否启用可视化页面
         */
        private boolean enabled = true;
    }
}
