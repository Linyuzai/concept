package com.github.linyuzai.plugin.core.storage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@NoArgsConstructor
public class SuffixStorageFilter implements PluginStorage.Filter {

    private List<String> suffixes = new CopyOnWriteArrayList<>();

    public SuffixStorageFilter(Collection<String> suffixes) {
        this.suffixes.addAll(suffixes);
    }

    @Override
    public boolean filter(String group, String name) {
        if (suffixes == null || suffixes.isEmpty()) {
            return true;
        }
        for (String suffix : suffixes) {
            if (name.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
