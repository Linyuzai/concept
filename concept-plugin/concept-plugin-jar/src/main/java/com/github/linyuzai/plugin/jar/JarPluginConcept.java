package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.AbstractPluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.extractor.PluginExtractor;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;

public class JarPluginConcept extends AbstractPluginConcept {

    public void add(URL url) {

    }

    public void add(File file) {

    }

    public void add(String path) {

    }

    public static class Builder {

        private PluginExtractor extractor;
    }
}
