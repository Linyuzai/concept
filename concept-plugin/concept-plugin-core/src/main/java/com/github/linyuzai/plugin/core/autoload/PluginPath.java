package com.github.linyuzai.plugin.core.autoload;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.function.Predicate;

@Data
@Builder(builderClassName = "Builder")
public class PluginPath {

    @NonNull
    private String path;

    private Predicate<String> filter;

    private boolean notifyCreate;

    private boolean notifyModify;

    private boolean notifyDelete;
}
