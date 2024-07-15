package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * jar插件
 */
public interface JarPlugin extends ZipPlugin {

    /**
     * 获得插件类加载器
     */
    PluginClassLoader getPluginClassLoader();

    void setPluginClassLoader(PluginClassLoader classLoader);

    @Data
    @EqualsAndHashCode(callSuper = true)
    class StandardMetadata extends ZipPlugin.StandardMetadata {

        private JarMetadata jar = new JarMetadata();

        @Data
        public static class JarMetadata {

            private String mode;
        }
    }

    /**
     * 加载模式
     */
    class Mode {

        /**
         * 文件随机访问
         */
        public static final String FILE = "FILE";

        /**
         * 数据流
         */
        public static final String STREAM = "STREAM";
    }
}
