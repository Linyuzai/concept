package com.github.linyuzai.plugin.core.factory;

import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 插件工厂抽象类
 */
@Getter
public abstract class AbstractPluginFactory implements PluginFactory, PluginMetadataFactory {

    private final List<PluginMetadata.Adapter> metadataAdapters = new CopyOnWriteArrayList<>();

    /**
     * 获得插件元数据适配器
     *
     * @param name 插件项名称
     * @return 插件元数据适配器
     */
    protected PluginMetadata.Adapter getMetadataAdapter(String name) {
        for (PluginMetadata.Adapter adapter : metadataAdapters) {
            if (adapter.support(name)) {
                return adapter;
            }
        }
        return null;
    }
}
