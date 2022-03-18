package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.List;

public class FileNameFilter extends AbstractPluginFilter<List<String>> {

    @Override
    public List<String> doFilter(List<String> plugins) {

        return null;
    }

    @Override
    public Object getKey() {
        return Plugin.FILE_NAMES;
    }
}
