package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.WatchServicePluginAutoLoader;
import com.github.linyuzai.plugin.core.concept.DefaultPluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.extract.OnPluginExtract;
import com.github.linyuzai.plugin.jar.extract.JarDynamicExtractor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

@Slf4j
public class ConceptPluginSample {

    /**
     * 插件提取配置
     */
    private final PluginConcept concept = new DefaultPluginConcept.Builder()
            //回调到标注了@OnPluginExtract的方法
            .addExtractors(new JarDynamicExtractor(this))
            .build();

    /**
     * 缓存设备类型和对应的操作对象
     */
    private final Map<String, DeviceOperation> operationMap = new ConcurrentHashMap<>();

    /**
     * 插件匹配回调
     *
     * @param operation  匹配到的 DeviceOperation 实例
     * @param deviceType 配置文件中定义的设备类型
     */
    @OnPluginExtract
    public void onPluginExtract(DeviceOperation operation, /*@PluginProperties("device.type")*/ String deviceType) {
        operationMap.put(deviceType, operation);
    }

    /**
     * 加载 jar 插件
     *
     * @param filePath jar 文件路径
     */
    public void load(String filePath) {
        concept.load(filePath);
    }

    /**
     * 操作一个设备
     *
     * @param device  设备对象
     * @param opType  操作类型
     * @param opValue 操作值
     * @return 操作结果
     */
    public OperationResult operate(Device device, String opType, Object opValue) {
        String deviceType = device.getDeviceType();
        DeviceOperation operation = operationMap.get(deviceType);
        if (operation == null) {
            throw new DeviceOperationNotFoundException(deviceType + " not found");
        }
        return operation.operate(device, opType, opValue);
    }

    /*private final PluginAutoLoader loader = new WatchServicePluginAutoLoader.Builder()
            .locations(new PluginPath.Builder()
                    .path("/Users/concept/plugin/")
                    .filter(it -> it.endsWith(".jar"))
                    .build())
            //
            .executor(Executors.newSingleThreadExecutor())
            //.onCreate(concept::load)
            .onNotify(new JarNotifier(concept))
            .onError(e -> log.error("Plugin auto load error", e))
            .build();

    *//**
     *
     *//*
    @PostConstruct
    private void start() {
        loader.start();
    }

    @PreDestroy
    private void stop() {
        loader.stop();
    }*/
}
