package com.github.linyuzai.extension.core.concept;

import com.github.linyuzai.extension.core.factory.ExtensionFactory;

import java.util.Collection;
import java.util.function.Predicate;

public interface ExtensionConcept {

    void register(Extension extension);

    void register(ExtensionFactory factory);

    <T extends Extension> T getExtension(Predicate<Extension> predicate);

    <T extends Extension> T getExtension(String extensionId);

    <T extends Extension> T getExtension(Class<T> extensionClass);

    Collection<Extension.ArgumentAndResult> extend(Collection<? extends Extension.Argument> arguments);
}
