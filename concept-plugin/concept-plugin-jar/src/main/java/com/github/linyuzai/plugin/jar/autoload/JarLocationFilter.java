package com.github.linyuzai.plugin.jar.autoload;

import com.github.linyuzai.plugin.zip.autoload.ZipLocationFilter;

public class JarLocationFilter extends ZipLocationFilter {

    @Override
    public boolean filter(String group, String name) {
        return name.endsWith(".jar") || super.filter(group, name);
    }
}
