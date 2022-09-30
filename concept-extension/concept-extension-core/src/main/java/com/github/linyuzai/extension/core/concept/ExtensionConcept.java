package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.factory.ExtensionFactory;

import java.util.Collection;

public interface ExtensionConcept {

    void register(Extension extension);

    void register(ExtensionFactory factory);

    Extension getExtension(String extensionId);

    Collection<Extension.ArgumentAndResult> extend(Collection<? extends Extension.Argument> arguments);
}
