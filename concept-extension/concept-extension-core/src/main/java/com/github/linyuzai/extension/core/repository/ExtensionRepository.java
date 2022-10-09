package com.github.linyuzai.extension.core.repository;

import com.github.linyuzai.extension.core.concept.Extension;

import java.util.stream.Stream;

public interface ExtensionRepository {

    void add(Extension extension);

    Extension remove(String extensionId);

    boolean exist(Extension extension);

    Extension get(String extensionId);

    Stream<Extension> stream();
}
