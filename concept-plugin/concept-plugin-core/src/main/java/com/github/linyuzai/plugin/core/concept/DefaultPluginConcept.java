package com.github.linyuzai.plugin.core.concept;

/**
 * Concept默认实现
 */
public class DefaultPluginConcept extends AbstractPluginConcept {

    public static class Builder extends AbstractBuilder<Builder, DefaultPluginConcept> {

        @Override
        protected DefaultPluginConcept create() {
            return new DefaultPluginConcept();
        }
    }
}
