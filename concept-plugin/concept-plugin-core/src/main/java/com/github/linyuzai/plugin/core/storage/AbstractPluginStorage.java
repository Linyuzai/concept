package com.github.linyuzai.plugin.core.storage;

import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Function;

@Getter
@Setter
public abstract class AbstractPluginStorage implements PluginStorage {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private PluginExecutor executor;

    @Override
    public void initialize() {

    }

    /**
     * 如果文件存在则顺序添加后缀
     */
    protected String generateName(String path, Function<String, Boolean> exist) {
        return generateName(path, exist, i -> "(" + i + ")");
    }

    protected String generateDeletedName(String path, Function<String, Boolean> exist) {
        return generateName(path, exist, i ->
                PluginStorage.DELETED + FORMATTER.format(LocalDateTime.now()));
    }

    /**
     * 生成文件名
     */
    protected String generateName(String name,
                                  Function<String, Boolean> exist,
                                  Function<Integer, String> generator) {
        int i = 1;
        int index = name.lastIndexOf(".");
        String prefix;
        String suffix;
        if (index == -1) {
            prefix = name;
            suffix = "";
        } else {
            prefix = name.substring(0, index);
            suffix = name.substring(index);
        }
        String generate = name;
        while (exist.apply(generate)) {
            generate = prefix + generator.apply(i) + suffix;
            if (Objects.equals(generate, name)) {
                throw new IllegalArgumentException("Generated name '" + name + "' is same as original name");
            }
            i++;
        }
        return generate;
    }
}
