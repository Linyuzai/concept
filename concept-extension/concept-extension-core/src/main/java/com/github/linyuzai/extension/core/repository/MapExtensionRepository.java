package com.github.linyuzai.extension.core.repository;

import com.github.linyuzai.extension.core.concept.AbstractExtension;
import com.github.linyuzai.extension.core.concept.Extension;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class MapExtensionRepository implements ExtensionRepository {

    private final Map<String, Extension> extensionMap = new ConcurrentHashMap<>();

    @Override
    public void add(Extension extension) {
        extensionMap.put(getExtensionId(extension), extension);
    }

    @Override
    public Extension remove(String extensionId) {
        return extensionMap.remove(extensionId);
    }

    @Override
    public boolean exist(Extension extension) {
        return extensionMap.containsKey(extension.getId());
    }

    @Override
    public Extension get(String extensionId) {
        return extensionMap.get(extensionId);
    }

    @Override
    public Stream<Extension> stream() {
        return extensionMap.values().stream();
    }

    private String getExtensionId(Extension extension) {
        String id = extension.getId();
        if (id == null) {
            String tempId = UUID.randomUUID().toString();
            if (extension instanceof AbstractExtension) {
                ((AbstractExtension) extension).setId(tempId);
            }
            return tempId;
        }
        return id;
    }
}
