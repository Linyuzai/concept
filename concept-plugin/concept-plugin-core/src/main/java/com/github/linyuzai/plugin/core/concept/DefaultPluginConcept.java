package com.github.linyuzai.plugin.core.concept;

public class DefaultPluginConcept extends AbstractPluginConcept {

    public static class Builder extends AbstractBuilder<Builder, DefaultPluginConcept> {

        @Override
        protected DefaultPluginConcept create() {
            return new DefaultPluginConcept();
        }
    }
}
