package com.github.linyuzai.plugin.autoconfigure.observable;

import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassAnnotation;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class ControllerPluginObservable extends BeanExtractor<Map<String, ?>>
        implements PluginObservable<String, Class<?>> {

    private final Map<String, Class<?>> controllerMap = createMap();

    @Override
    public Class<?> get(String key) {
        return controllerMap.get(key);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Class<?>> action) {
        controllerMap.forEach(action);
    }

    @Override
    public void onExtract(@PluginClassAnnotation(Controller.class) Map<String, ?> controllers, PluginContext context) {
        List<Runnable> destroyList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : controllers.entrySet()) {
            String path = entry.getKey();
            Object controller = entry.getValue();
            Class<?> type = controller.getClass();
            doRegister(type, controller, destroyList);
            controllerMap.put(path, type);
            context.getPlugin().addDestroyListener(p -> destroyList.forEach(Runnable::run));
        }
    }

    protected Map<String, Class<?>> createMap() {
        return new ConcurrentHashMap<>();
    }

    protected abstract void doRegister(Class<?> type, Object controller, List<Runnable> destroyList);
}
