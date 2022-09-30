package com.github.linyuzai.extension.core.dependence;

import com.github.linyuzai.extension.core.concept.Extension;

public interface DependenceProvider {

    Extension getDependenceExtension(String extensionId);

    Extension getDependenceExtension(Class<? extends Extension> clazz);
}
