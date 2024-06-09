package com.github.linyuzai.plugin.core.util;

import com.github.linyuzai.plugin.core.autoload.PluginAutoEvent;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.event.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

/**
 * 插件加载日志
 */
@RequiredArgsConstructor
public class PluginLoadLogger implements PluginEventListener {

    @NonNull
    private final Consumer<String> infoLogger;

    @Override
    public void onEvent(Object event) {
        if (event instanceof PluginEvent) {
            Plugin plugin = ((PluginEvent) event).getPlugin();
            if (event instanceof PluginAutoEvent) {
                /*if (event instanceof PluginAutoLoadEvent) {
                    info("Auto load " + plugin);
                } else if (event instanceof PluginAutoReloadEvent) {
                    info("Auto reload " + plugin);
                } else if (event instanceof PluginAutoUnloadEvent) {
                    info("Auto unload " + plugin);
                }*/
            } else if (event instanceof PluginContextEvent) {
                /*if (event instanceof PluginResolvedEvent) {
                    Object resolvedKey = ((PluginResolvedEvent) event).getResolvedKey();
                    Object dependedKey = ((PluginResolvedEvent) event).getDependedKey();
                    String resolver = ((PluginResolvedEvent) event).getResolver().getClass().getSimpleName();
                    if (dependedKey == null) {
                        info("Resolve " + resolvedKey + " use " + resolver);
                    } else {
                        info("Resolve " + resolvedKey + " depend on " + dependedKey + " use " + resolver);
                    }
                } else if (event instanceof PluginFilteredEvent) {
                    String filter = ((PluginFilteredEvent) event).getFilter().getClass().getSimpleName();
                    info("Filter use " + filter);
                } else if (event instanceof PluginMatchedEvent) {
                    String matcher = ((PluginMatchedEvent) event).getMatcher().getClass().getSimpleName();
                    info("Match use " + matcher);
                } else if (event instanceof PluginConvertedEvent) {
                    String convertor = ((PluginConvertedEvent) event).getConvertor().getClass().getSimpleName();
                    info("Convert use " + convertor);
                } else if (event instanceof PluginFormattedEvent) {
                    String formatter = ((PluginFormattedEvent) event).getFormatter().getClass().getSimpleName();
                    info("Format use " + formatter);
                } else if (event instanceof PluginExtractedEvent) {
                    if (event instanceof DynamicPluginExtractedEvent) {
                        String extractor = ((DynamicPluginExtractedEvent) event).getMethod().getName();
                        info("Extract to method " + extractor);
                    } else {
                        Class<?> c = ((PluginExtractedEvent) event).getExtractor().getClass();
                        while (c != null) {
                            if (c.getSimpleName().isEmpty()) {
                                c = c.getSuperclass();
                            } else {
                                String extractor = c.getSimpleName();
                                info("Extract use " + extractor);
                                break;
                            }
                        }
                    }
                }*/
            } else {
                if (event instanceof PluginCreatedEvent) {
                    info("Create " + plugin);
                } else if (event instanceof PluginPreparedEvent) {
                    info("Prepared");
                } else if (event instanceof PluginReleasedEvent) {
                    info("Released");
                } else if (event instanceof PluginLoadedEvent) {
                    //info("Load " + plugin);
                } else if (event instanceof PluginUnloadedEvent) {
                    info("Unload " + plugin);
                }
            }
        }
    }

    public String appendTag(String msg) {
        return "Plugin >> " + msg;
    }

    public void info(String msg) {
        infoLogger.accept(appendTag(msg));
    }
}
