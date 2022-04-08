package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.function.Predicate;

/**
 * 监听插件位置
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginLocation {

    /**
     * 路径
     */
    private String path;

    /**
     * 过滤器
     */
    private Predicate<String> filter;

    /**
     * 触发创建回调
     */
    private boolean notifyCreate;

    /**
     * 触发修改回调
     */
    private boolean notifyModify;

    /**
     * 触发删除回调
     */
    private boolean notifyDelete;

    public static final class Builder {
        private String path;
        private Predicate<String> filter;
        private boolean notifyCreate = true;
        private boolean notifyModify = true;
        private boolean notifyDelete = true;

        /**
         * 设置监听的路径
         *
         * @param path 监听的路径
         * @return {@link Builder}
         */
        public Builder path(String path) {
            this.path = path;
            return this;
        }

        /**
         * 设置文件名过滤器
         *
         * @param filter 文件名过滤器
         * @return {@link Builder}
         */
        public Builder filter(Predicate<String> filter) {
            this.filter = filter;
            return this;
        }

        /**
         * 设置是否监听文件新增
         *
         * @param notifyCreate 是否监听文件新增
         * @return {@link Builder}
         */
        public Builder notifyCreate(boolean notifyCreate) {
            this.notifyCreate = notifyCreate;
            return this;
        }

        /**
         * 设置是否监听文件修改
         *
         * @param notifyModify 是否监听文件修改
         * @return {@link Builder}
         */
        public Builder notifyModify(boolean notifyModify) {
            this.notifyModify = notifyModify;
            return this;
        }

        /**
         * 设置是否监听文件删除
         *
         * @param notifyDelete 是否监听文件删除
         * @return {@link Builder}
         */
        public Builder notifyDelete(boolean notifyDelete) {
            this.notifyDelete = notifyDelete;
            return this;
        }

        public PluginLocation build() {
            if (path == null || path.isEmpty()) {
                throw new PluginException("Path is null or empty");
            }
            if (path.endsWith(File.separator)) {
                path = path.substring(0, path.length() - 1);
            }
            return new PluginLocation(path, filter, notifyCreate, notifyModify, notifyDelete);
        }
    }
}
