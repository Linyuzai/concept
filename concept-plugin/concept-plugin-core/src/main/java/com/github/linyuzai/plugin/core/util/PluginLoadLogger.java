package com.github.linyuzai.plugin.core.util;

import com.github.linyuzai.plugin.core.autoload.PluginAutoEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoLoadEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoReloadEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoUnloadEvent;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.convert.PluginConvertedEvent;
import com.github.linyuzai.plugin.core.event.*;
import com.github.linyuzai.plugin.core.extract.DynamicPluginExtractedEvent;
import com.github.linyuzai.plugin.core.extract.PluginExtractedEvent;
import com.github.linyuzai.plugin.core.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.filter.PluginFilteredEvent;
import com.github.linyuzai.plugin.core.format.PluginFormattedEvent;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.match.PluginMatchedEvent;
import com.github.linyuzai.plugin.core.resolve.PluginResolvedEvent;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.function.Consumer;

/**
 * 插件加载日志
 */
@AllArgsConstructor
public class PluginLoadLogger implements PluginEventListener {

    @NonNull
    private final Consumer<String> infoLogger;

    private final boolean logDetail;

    public PluginLoadLogger(@NonNull Consumer<String> infoLogger) {
        this(infoLogger, false);
    }

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
