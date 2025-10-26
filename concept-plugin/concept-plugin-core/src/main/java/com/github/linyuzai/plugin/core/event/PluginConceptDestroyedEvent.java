package com.github.linyuzai.plugin.core.event;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Concept销毁事件
 */
@Getter
@RequiredArgsConstructor
public class PluginConceptDestroyedEvent {

    private final PluginConcept concept;
}
