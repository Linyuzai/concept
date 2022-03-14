package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.*;

import java.util.function.Predicate;

@Data
public class PluginPath {

    private String path;

    private Predicate<String> filter;

    private boolean notifyCreate;

    private boolean notifyModify;

    private boolean notifyDelete;

    public static final class Builder {
        private String path;
        private Predicate<String> filter;
        private boolean notifyCreate = true;
        private boolean notifyModify = true;
        private boolean notifyDelete = true;

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder filter(Predicate<String> filter) {
            this.filter = filter;
            return this;
        }

        public Builder notifyCreate(boolean notifyCreate) {
            this.notifyCreate = notifyCreate;
            return this;
        }

        public Builder notifyModify(boolean notifyModify) {
            this.notifyModify = notifyModify;
            return this;
        }

        public Builder notifyDelete(boolean notifyDelete) {
            this.notifyDelete = notifyDelete;
            return this;
        }

        public PluginPath build() {
            if (path == null || path.isEmpty()) {
                throw new PluginException("Path is null or empty");
            }
            PluginPath pluginPath = new PluginPath();
            pluginPath.setPath(path);
            pluginPath.setFilter(filter);
            pluginPath.setNotifyCreate(notifyCreate);
            pluginPath.setNotifyModify(notifyModify);
            pluginPath.setNotifyDelete(notifyDelete);
            return pluginPath;
        }
    }
}
