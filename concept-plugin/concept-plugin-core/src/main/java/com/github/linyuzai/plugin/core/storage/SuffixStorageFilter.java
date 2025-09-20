package com.github.linyuzai.plugin.core.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuffixStorageFilter implements PluginStorage.Filter {

    private Collection<String> suffixes;

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
